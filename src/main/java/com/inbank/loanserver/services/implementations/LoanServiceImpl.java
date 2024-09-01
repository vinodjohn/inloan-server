package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.dtos.LoanDecisionStatus;
import com.inbank.loanserver.dtos.LoanProposal;
import com.inbank.loanserver.dtos.LoanResponse;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.models.LoanOfferStatus;
import com.inbank.loanserver.services.KeyValueStoreService;
import com.inbank.loanserver.services.LoanOfferService;
import com.inbank.loanserver.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of LoanService
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private KeyValueStoreService keyValueStoreService;

    @Autowired
    private LoanOfferService loanOfferService;

    @Override
    public LoanResponse getLoanDecision(LoanApplication loanApplication) throws KeyValueStoreNotFoundException {
        int creditModifier = loanApplication.getPerson().getCreditModifier().getValue();

        // If a person has a debt
        if (creditModifier == 0) {
            return getLoanResponse(false, Collections.emptySet());
        }

        float creditScore =
                ((float) creditModifier / loanApplication.getRequestAmount()) * loanApplication.getRequestPeriod();
        var creditCoefficient = keyValueStoreService.getCreditCoefficient().getValue();
        var maxLoanPeriod = keyValueStoreService.getMaximumLoanPeriod().getValue();
        var maxLoanAmount = keyValueStoreService.getMaximumLoanAmount().getValue();
        Set<LoanOffer> loanOffers = new HashSet<>();

        // If less-eligible
        if (creditScore < creditCoefficient) {
            var minLoanAmount = keyValueStoreService.getMinimumLoanAmount().getValue();
            var sum = Math.round(creditScore * loanApplication.getRequestAmount());
            var minLoanPeriod = keyValueStoreService.getMinimumLoanPeriod().getValue();

            if (sum > minLoanAmount) {
                // Offer with suitable amount based on requested period
                LoanOffer loanOfferBasic = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        sum, loanApplication.getRequestPeriod(), maxLoanPeriod));
                loanOffers.add(loanOfferBasic);

                minLoanAmount = sum;
            }

            var potentialPeriod = (creditCoefficient * loanApplication.getRequestAmount()) / creditModifier;

            if (potentialPeriod <= maxLoanPeriod) {
                // Offer with requested amount and suitable period based on credit score and credit coefficient
                LoanOffer loanOfferPlus = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        loanApplication.getRequestAmount(), potentialPeriod, maxLoanPeriod));
                loanOffers.add(loanOfferPlus);

                minLoanAmount = loanApplication.getRequestAmount();
            }

            // Offer with maximum amount based on credit modifier and credit coefficient with suitable period
            LoanOffer loanOfferAdv = loanOfferService.createLoanOffer(getAdvancedLoanOffer(loanApplication,
                    creditScore, creditCoefficient, minLoanAmount, maxLoanAmount, minLoanPeriod, maxLoanPeriod));
            loanOffers.add(loanOfferAdv);

        } else {
            // Offer with requested amount and requested period
            LoanOffer loanOfferBasic = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                    loanApplication.getRequestAmount(), loanApplication.getRequestPeriod(), maxLoanPeriod));
            loanOffers.add(loanOfferBasic);

            var maxSum = Math.round(creditScore * loanApplication.getRequestAmount());

            if (maxSum < maxLoanAmount) {
                // Offer with additional amount based on requested period
                LoanOffer loanOfferPlus = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        maxSum, loanApplication.getRequestPeriod(), maxLoanPeriod));
                loanOffers.add(loanOfferPlus);

                // Offer with maximum amount based on credit modifier and credit coefficient with suitable period
                LoanOffer loanOfferAdv = loanOfferService.createLoanOffer(getAdvancedLoanOffer(loanApplication,
                        creditScore, creditCoefficient, maxSum, maxLoanAmount, loanApplication.getRequestPeriod(),
                        maxLoanPeriod));
                loanOffers.add(loanOfferAdv);
            } else {
                // Offer with maximum amount with maximum period
                LoanOffer loanOfferMax = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        maxLoanAmount, loanApplication.getRequestPeriod(), maxLoanPeriod));
                loanOffers.add(loanOfferMax);
            }
        }

        return getLoanResponse(true, loanOffers);
    }

    // PRIVATE METHODS //
    private LoanOffer getLoanOffer(LoanApplication loanApplication, float creditScore, int loanAmount,
                                   int minLoanPeriod, int maxLoanPeriod) {
        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setLoanApplication(loanApplication);
        loanOffer.setCreditScore(creditScore);
        loanOffer.setLoanAmount(loanAmount);
        loanOffer.setMinPeriod(minLoanPeriod);
        loanOffer.setMaxPeriod(maxLoanPeriod);
        loanOffer.setLoanOfferStatus(LoanOfferStatus.PENDING);

        return loanOffer;
    }

    private LoanOffer getAdvancedLoanOffer(LoanApplication loanApplication, float creditScore, float creditCoefficient,
                                           int minLoanAmount, int maxLoanAmount, int minLoanPeriod, int maxLoanPeriod) {
        int creditModifier = loanApplication.getPerson().getCreditModifier().getValue(),
                bestLoanAmount = 0, bestLoanPeriod = 0, highestLoan = 0,
                loanIncrementor = Math.round(((float) creditModifier / 2));

        for (int loanAmount = minLoanAmount; loanAmount <= maxLoanAmount; loanAmount += loanIncrementor) {
            int loanPeriod = Math.round(loanAmount * creditCoefficient) / creditModifier;

            if (loanPeriod >= minLoanPeriod && loanPeriod <= maxLoanPeriod) {
                int loan = loanAmount * loanPeriod;

                if (loan > highestLoan) {
                    highestLoan = loan;
                    bestLoanAmount = loanAmount;
                    bestLoanPeriod = loanPeriod;
                }
            }
        }

        return getLoanOffer(loanApplication, creditScore, bestLoanAmount, bestLoanPeriod, maxLoanPeriod);
    }

    private LoanProposal convertLoanOfferToLoanProposal(LoanOffer loanOffer) {
        return new LoanProposal(loanOffer.getId(), loanOffer.getLoanAmount(), loanOffer.getMinPeriod(),
                loanOffer.getMaxPeriod());
    }

    private LoanResponse getLoanResponse(boolean isPositive, Set<LoanOffer> loanOffers) {
        Set<LoanProposal> loanProposals = loanOffers.stream()
                .sorted(Comparator.comparing(LoanOffer::getLoanAmount).reversed())
                .map(this::convertLoanOfferToLoanProposal)
                .collect(Collectors.toUnmodifiableSet());

        return new LoanResponse(isPositive ? LoanDecisionStatus.POSITIVE.name() : LoanDecisionStatus.NEGATIVE.name(),
                loanProposals);
    }
}

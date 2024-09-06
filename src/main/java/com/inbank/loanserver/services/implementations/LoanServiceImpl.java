package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.dtos.LoanDecisionStatus;
import com.inbank.loanserver.dtos.LoanOfferDto;
import com.inbank.loanserver.dtos.LoanResponse;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.models.LoanOfferStatus;
import com.inbank.loanserver.models.LoanOfferType;
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
                        sum, loanApplication.getRequestPeriod(), maxLoanPeriod, LoanOfferType.BASIC));
                loanOffers.add(loanOfferBasic);

                minLoanAmount = sum;
            }

            var potentialPeriod = (creditCoefficient * loanApplication.getRequestAmount()) / creditModifier;

            if (potentialPeriod <= maxLoanPeriod) {
                // Offer with requested amount and suitable period based on credit score and credit coefficient
                LoanOfferType loanOfferType = loanOffers.isEmpty() ? LoanOfferType.BASIC : LoanOfferType.PLUS;
                LoanOffer loanOfferPlus = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        loanApplication.getRequestAmount(), potentialPeriod, maxLoanPeriod, loanOfferType));
                loanOffers.add(loanOfferPlus);

                minLoanAmount = loanApplication.getRequestAmount();
            }

            // Offer with maximum amount based on credit modifier and credit coefficient with suitable period
            LoanOfferType loanOfferType;

            if (loanOffers.isEmpty()) {
                loanOfferType = LoanOfferType.BASIC;
            } else if (loanOffers.size() == 1) {
                loanOfferType = LoanOfferType.PLUS;
            } else {
                loanOfferType = LoanOfferType.MAX;
            }

            LoanOffer loanOfferAdv = getAdvancedLoanOffer(loanApplication,
                    creditScore, creditCoefficient, minLoanAmount, maxLoanAmount, minLoanPeriod, maxLoanPeriod,
                    loanOfferType);

            if (loanOfferAdv.getLoanAmount() != loanApplication.getRequestAmount()
                    && loanOfferAdv.getMinPeriod() != potentialPeriod && loanOfferAdv.getMaxPeriod() != maxLoanPeriod) {
                loanOfferAdv = loanOfferService.createLoanOffer(loanOfferAdv);
                loanOffers.add(loanOfferAdv);
            }
        } else {
            // Offer with requested amount and requested period
            LoanOffer loanOfferBasic = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                    loanApplication.getRequestAmount(), loanApplication.getRequestPeriod(), maxLoanPeriod,
                    LoanOfferType.BASIC));
            loanOffers.add(loanOfferBasic);

            var maxSum = Math.round(creditScore * loanApplication.getRequestAmount());

            if (maxSum < maxLoanAmount) {
                // Offer with additional amount based on requested period
                LoanOffer loanOfferPlus = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        maxSum, loanApplication.getRequestPeriod(), maxLoanPeriod, LoanOfferType.PLUS));
                loanOffers.add(loanOfferPlus);

                // Offer with maximum amount based on credit modifier and credit coefficient with suitable period
                LoanOffer loanOfferAdv = getAdvancedLoanOffer(loanApplication,
                        creditScore, creditCoefficient, maxSum, maxLoanAmount, loanApplication.getRequestPeriod(),
                        maxLoanPeriod, LoanOfferType.MAX);

                if (loanOfferAdv.getLoanAmount() != maxSum
                        && loanOfferAdv.getMinPeriod() != loanApplication.getRequestPeriod()
                        && loanOfferAdv.getMaxPeriod() != maxLoanPeriod) {
                    loanOfferAdv = loanOfferService.createLoanOffer(loanOfferAdv);
                    loanOffers.add(loanOfferAdv);
                }
            } else {
                // Offer with maximum amount with maximum period
                LoanOffer loanOfferMax = loanOfferService.createLoanOffer(getLoanOffer(loanApplication, creditScore,
                        maxLoanAmount, loanApplication.getRequestPeriod(), maxLoanPeriod, LoanOfferType.MAX));
                loanOffers.add(loanOfferMax);
            }
        }

        return getLoanResponse(true, loanOffers);
    }

    // PRIVATE METHODS //
    private LoanOffer getLoanOffer(LoanApplication loanApplication, float creditScore, int loanAmount,
                                   int minLoanPeriod, int maxLoanPeriod, LoanOfferType loanOfferType) {
        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setLoanApplication(loanApplication);
        loanOffer.setCreditScore(creditScore);
        loanOffer.setLoanAmount(loanAmount);
        loanOffer.setMinPeriod(minLoanPeriod);
        loanOffer.setMaxPeriod(maxLoanPeriod);
        loanOffer.setLoanOfferStatus(LoanOfferStatus.PENDING);
        loanOffer.setLoanOfferType(loanOfferType);

        return loanOffer;
    }

    private LoanOffer getAdvancedLoanOffer(LoanApplication loanApplication, float creditScore, float creditCoefficient,
                                           int minLoanAmount, int maxLoanAmount, int minLoanPeriod, int maxLoanPeriod
            , LoanOfferType loanOfferType) {
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

        return getLoanOffer(loanApplication, creditScore, bestLoanAmount, bestLoanPeriod, maxLoanPeriod, loanOfferType);
    }

    private LoanOfferDto convertLoanOfferToLoanOfferDto(LoanOffer loanOffer) {
        return new LoanOfferDto(loanOffer.getId(), loanOffer.getLoanOfferType().name(), loanOffer.getLoanAmount(),
                loanOffer.getMinPeriod(), loanOffer.getMaxPeriod());
    }

    private LoanResponse getLoanResponse(boolean isPositive, Set<LoanOffer> loanOffers) {
        Set<LoanOfferDto> loanOfferDtos = loanOffers.stream()
                .sorted(Comparator.comparing(LoanOffer::getLoanAmount).reversed())
                .map(this::convertLoanOfferToLoanOfferDto)
                .collect(Collectors.toUnmodifiableSet());

        return new LoanResponse(isPositive ? LoanDecisionStatus.POSITIVE.name() : LoanDecisionStatus.NEGATIVE.name(),
                loanOfferDtos);
    }
}

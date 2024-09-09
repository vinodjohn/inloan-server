package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.models.LoanOfferStatus;
import com.inbank.loanserver.repositories.LoanOfferRepository;
import com.inbank.loanserver.services.LoanOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inbank.loanserver.utils.LoanUtils.getSortOfColumn;

/**
 * Implementation of LoanOfferService
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Service
@Transactional
public class LoanOfferServiceImpl implements LoanOfferService {
    @Autowired
    private LoanOfferRepository loanOfferRepository;

    @Override
    public LoanOffer createLoanOffer(LoanOffer loanOffer) {
        loanOffer.setActive(true);
        return loanOfferRepository.saveAndFlush(loanOffer);
    }

    @Override
    public LoanOffer findLoanOfferById(UUID id) throws LoanOfferNotFoundException {
        Optional<LoanOffer> optionalLoanOffer = loanOfferRepository.findById(id);

        if (optionalLoanOffer.isEmpty()) {
            throw new LoanOfferNotFoundException(id);
        }

        return optionalLoanOffer.get();
    }

    @Override
    public Page<LoanOffer> findLoanOffersByLoanApplication(Pageable pageable, LoanApplication loanApplication) {
        return loanOfferRepository.findAllByLoanApplication(loanApplication, pageable);
    }

    @Override
    public Page<LoanOffer> findAllLoanOffers(Pageable pageable) {
        return loanOfferRepository.findAll(pageable);
    }

    @Override
    public LoanOffer updateLoanOffer(LoanOffer loanOffer) throws LoanOfferNotFoundException {
        if (findLoanOfferById(loanOffer.getId()) != null) {
            return loanOfferRepository.saveAndFlush(loanOffer);
        }

        return null;
    }

    @Override
    public void deleteLoanOfferById(UUID id) throws LoanOfferNotFoundException {
        LoanOffer loanOffer = findLoanOfferById(id);
        loanOffer.setActive(false);
        loanOfferRepository.saveAndFlush(loanOffer);
    }

    @Override
    public void restoreLoanOfferById(UUID id) throws LoanOfferNotFoundException {
        LoanOffer loanOffer = findLoanOfferById(id);
        loanOffer.setActive(true);
        loanOfferRepository.saveAndFlush(loanOffer);
    }

    @Override
    public void changeLoanOfferSiblingStatus(LoanApplication loanApplication, LoanOffer loanOffer) throws LoanOfferNotFoundException {
        Page<LoanOffer> loanOfferPage = findLoanOffersByLoanApplication(PageRequest.of(0
                , 10, getSortOfColumn("createdDate", "desc")), loanApplication);

        loanOfferPage.get()
                .filter(lo -> !lo.getId().equals(loanOffer.getId()))
                .forEach(lo -> {
                    lo.setLoanOfferStatus(LoanOfferStatus.DECLINED);
                    loanOfferRepository.saveAndFlush(lo);
                });
    }
}

package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.repositories.LoanOfferRepository;
import com.inbank.loanserver.services.LoanOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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
        return loanOfferRepository.save(loanOffer);
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
            return loanOfferRepository.save(loanOffer);
        }

        return null;
    }

    @Override
    public void deleteLoanOfferById(UUID id) throws LoanOfferNotFoundException {
        LoanOffer loanOffer = findLoanOfferById(id);
        loanOffer.setActive(false);
        loanOfferRepository.save(loanOffer);
    }

    @Override
    public void restoreLoanOfferById(UUID id) throws LoanOfferNotFoundException {
        LoanOffer loanOffer = findLoanOfferById(id);
        loanOffer.setActive(true);
        loanOfferRepository.save(loanOffer);
    }
}

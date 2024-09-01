package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.LoanApplicationNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.repositories.LoanApplicationRepository;
import com.inbank.loanserver.services.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of LoanApplicationService
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Service
@Transactional
public class LoanApplicationServiceImpl implements LoanApplicationService {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        loanApplication.setActive(true);
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public LoanApplication findLoanApplicationById(UUID id) throws LoanApplicationNotFoundException {
        Optional<LoanApplication> optionalLoanApplication = loanApplicationRepository.findById(id);

        if (optionalLoanApplication.isEmpty()) {
            throw new LoanApplicationNotFoundException(id);
        }

        return optionalLoanApplication.get();
    }

    @Override
    public Page<LoanApplication> findLoanApplicationsByPerson(Pageable pageable, Person person) {
        return loanApplicationRepository.findAllByPerson(person, pageable);
    }

    @Override
    public Page<LoanApplication> findAllLoanApplications(Pageable pageable) {
        return loanApplicationRepository.findAll(pageable);
    }

    @Override
    public LoanApplication updateLoanApplication(LoanApplication loanApplication) throws LoanApplicationNotFoundException {
        if (findLoanApplicationById(loanApplication.getId()) != null) {
           return loanApplicationRepository.save(loanApplication);
        }

        return null;
    }

    @Override
    public void deleteLoanApplicationById(UUID id) throws LoanApplicationNotFoundException {
        LoanApplication loanApplication = findLoanApplicationById(id);
        loanApplication.setActive(false);
        loanApplicationRepository.save(loanApplication);
    }

    @Override
    public void restoreLoanApplicationById(UUID id) throws LoanApplicationNotFoundException {
        LoanApplication loanApplication = findLoanApplicationById(id);
        loanApplication.setActive(true);
        loanApplicationRepository.save(loanApplication);
    }
}

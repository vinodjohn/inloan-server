package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.LoanContractNotFoundException;
import com.inbank.loanserver.models.LoanContract;
import com.inbank.loanserver.repositories.LoanContractRepository;
import com.inbank.loanserver.services.LoanContractService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of LoanContractService
 *
 * @author vinodjohn
 * @created 06.09.2024
 */
@Service
@Transactional
public class LoanContractServiceImpl implements LoanContractService {
    @Autowired
    private LoanContractRepository loanContractRepository;

    @Override
    public LoanContract createLoanContract(LoanContract loanContract) {
        loanContract.setActive(true);
        return loanContractRepository.saveAndFlush(loanContract);
    }

    @Override
    public LoanContract findLoanContractById(UUID id) throws LoanContractNotFoundException {
        Optional<LoanContract> optionalLoanContract = loanContractRepository.findById(id);

        if (optionalLoanContract.isEmpty()) {
            throw new LoanContractNotFoundException(id);
        }

        return optionalLoanContract.get();
    }

    @Override
    public Page<LoanContract> findLoanContractsByPerson(Pageable pageable, UUID personId) {
        return loanContractRepository.findAllByPerson(personId, pageable);
    }

    @Override
    public Page<LoanContract> findAllLoanContracts(Pageable pageable) {
        return loanContractRepository.findAll(pageable);
    }

    @Override
    public LoanContract updateLoanContract(LoanContract loanContract) throws LoanContractNotFoundException {
        if (findLoanContractById(loanContract.getId()) != null) {
            return loanContractRepository.saveAndFlush(loanContract);
        }

        return null;
    }

    @Override
    public void deleteLoanContractById(UUID id) throws LoanContractNotFoundException {
        LoanContract loanContract = findLoanContractById(id);
        loanContract.setActive(false);
        loanContractRepository.saveAndFlush(loanContract);
    }

    @Override
    public void restoreLoanContractById(UUID id) throws LoanContractNotFoundException {
        LoanContract loanContract = findLoanContractById(id);
        loanContract.setActive(true);
        loanContractRepository.saveAndFlush(loanContract);
    }
}

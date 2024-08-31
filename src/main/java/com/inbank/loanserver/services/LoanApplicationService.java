package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.LoanApplicationNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Loan Book related operations
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface LoanApplicationService {
    /**
     * To create a new loan application
     *
     * @param loanApplication LoanApplication
     * @return LoanApplication
     */
    LoanApplication createLoanApplication(LoanApplication loanApplication);

    /**
     * To find a loan application by ID
     *
     * @param id ID of a LoanApplication
     * @return LoanApplication
     */
    LoanApplication findLoanApplicationById(UUID id) throws LoanApplicationNotFoundException;

    /**
     * To find all loan applications of a person
     *
     * @param pageable Pageable of Loan Applications
     * @param person   Person of loan application
     * @return Page of loan application
     */
    Page<LoanApplication> findLoanApplicationsByPerson(Pageable pageable, Person person) throws LoanApplicationNotFoundException;

    /**
     * To find all loan applications
     *
     * @param pageable Pageable of Loan Applications
     * @return page of loan application
     */
    Page<LoanApplication> findAllLoanApplications(Pageable pageable);

    /**
     * To update an existing loan application
     *
     * @param loanApplication LoanApplication
     * @return LoanApplication
     */
    LoanApplication updateLoanApplication(LoanApplication loanApplication) throws LoanApplicationNotFoundException;

    /**
     * To delete a loan application by ID
     *
     * @param id LoanApplication ID
     */
    void deleteLoanApplicationById(UUID id) throws LoanApplicationNotFoundException;

    /**
     * To restore a loan application by ID
     *
     * @param id LoanApplication ID
     */
    void restoreLoanApplicationById(UUID id) throws LoanApplicationNotFoundException;
}

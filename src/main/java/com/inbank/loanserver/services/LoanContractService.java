package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.LoanContractNotFoundException;
import com.inbank.loanserver.models.LoanContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle all Loan Contract related operations
 *
 * @author vinodjohn
 * @created 05.09.2024
 */
public interface LoanContractService {

    /**
     * Create a new Loan Contract.
     *
     * @param loanContract The LoanContract object to be created.
     * @return The created LoanContract object.
     */
    LoanContract createLoanContract(LoanContract loanContract);

    /**
     * To find a Loan Contract by ID
     *
     * @param id ID of a LoanContract
     * @return LoanContract
     */
    LoanContract findLoanContractById(UUID id) throws LoanContractNotFoundException;

    /**
     * Finds loan contracts associated with a specific person.
     *
     * @param pageable The pagination information.
     * @param personId The UUID of the person whose loan contracts are to be found.
     * @return A pageable list of loan contracts associated with the given person.
     * @throws LoanContractNotFoundException If no loan contracts are found for the person.
     */
    Page<LoanContract> findLoanContractsByPerson(Pageable pageable, UUID personId) throws LoanContractNotFoundException;

    /**
     * To find all loan contracts
     *
     * @param pageable Pageable of Loan Contracts
     * @return page of loan contract
     */
    Page<LoanContract> findAllLoanContracts(Pageable pageable);

    /**
     * To update an existing loan contract
     *
     * @param loanContract LoanContract
     * @return LoanContract
     */
    LoanContract updateLoanContract(LoanContract loanContract) throws LoanContractNotFoundException;

    /**
     * To delete a loan contract by ID
     *
     * @param id LoanContract ID
     */
    void deleteLoanContractById(UUID id) throws LoanContractNotFoundException;

    /**
     * To restore a loan contract by ID
     *
     * @param id LoanContract ID
     */
    void restoreLoanContractById(UUID id) throws LoanContractNotFoundException;
}

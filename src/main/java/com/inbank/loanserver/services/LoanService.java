package com.inbank.loanserver.services;

import com.inbank.loanserver.dtos.LoanResponse;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.LoanApplication;

/**
 * Service to handle Loan related operations
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface LoanService {
    /**
     * To get the loan decision based on loan request
     *
     * @param loanApplication Loan Application
     * @return Loan Response
     */
    LoanResponse getLoanDecision(LoanApplication loanApplication) throws KeyValueStoreNotFoundException;
}

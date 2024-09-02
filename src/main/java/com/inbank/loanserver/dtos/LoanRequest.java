package com.inbank.loanserver.dtos;

import com.inbank.loanserver.utils.constraints.ValidLoanRequest;

/**
 * Loan request DTO
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@ValidLoanRequest
public record LoanRequest(int loanAmount, int loanPeriod) {
}

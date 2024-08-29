package com.inbank.loanserver.dtos;

/**
 * Loan request DTO
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public record LoanRequest(String personalIdCode, int loanAmount, int loanPeriod) {
}

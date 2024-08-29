package com.inbank.loanserver.dtos;

/**
 * Loan Response DTO
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public record LoanResponse(String loanDecisionStatus, int loanDecisionMinAmount, int loanDecisionMaxAmount,
                           int loanDecisionMinPeriod, int loanDecisionMaxPeriod) {
}

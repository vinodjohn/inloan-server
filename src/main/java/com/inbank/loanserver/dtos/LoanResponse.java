package com.inbank.loanserver.dtos;

import java.util.Set;

/**
 * Loan Response DTO
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public record LoanResponse(String loanDecisionStatus, Set<LoanOfferDto> loanOfferDtos) {
}

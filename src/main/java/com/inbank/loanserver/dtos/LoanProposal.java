package com.inbank.loanserver.dtos;

import java.util.UUID;

/**
 * Loan Proposal DTO
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
public record LoanProposal(UUID loanOfferId, int loanAmount, int minPeriod, int maxPeriod) {
}

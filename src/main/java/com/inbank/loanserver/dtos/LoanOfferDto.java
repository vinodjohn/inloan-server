package com.inbank.loanserver.dtos;

import java.util.UUID;

/**
 * Loan Offer DTO
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
public record LoanOfferDto(UUID loanOfferId, String type, int loanAmount, int minPeriod, int maxPeriod) {
}

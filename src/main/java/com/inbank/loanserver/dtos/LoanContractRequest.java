package com.inbank.loanserver.dtos;

import java.util.UUID;

/**
 * LoanContract Request DTO
 *
 * @author vinodjohn
 * @created 05.09.2024
 */
public record LoanContractRequest(UUID loanOfferId, float interestRate, int period, float monthlyAmount) {
}

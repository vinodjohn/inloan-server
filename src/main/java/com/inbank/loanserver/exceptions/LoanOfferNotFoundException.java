package com.inbank.loanserver.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Loan Offer's unavailability
 * 
 * @author vinodjohn
 * @created 30.08.2024
 */
public class LoanOfferNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoanOfferNotFoundException(UUID id) {
        super(MessageFormat.format("Loan Offer not found! (ID: {0})", id));
    }

    public LoanOfferNotFoundException(String personName, UUID loanApplicationId) {
        super(MessageFormat.format("Loan Offer not found! (Person: {0}, Loan application ID: {1})",
                personName, loanApplicationId));
    }
}

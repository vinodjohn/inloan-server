package com.inbank.loanserver.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Loan Application's unavailability
 * 
 * @author vinodjohn
 * @created 30.08.2024
 */
public class LoanApplicationNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoanApplicationNotFoundException(UUID id) {
        super(MessageFormat.format("Loan Application not found! (ID: {0}", id));
    }

    public LoanApplicationNotFoundException(String personName) {
        super(MessageFormat.format("Loan Application not found! (Person: {0}", personName));
    }
}

package com.inbank.loanserver.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Loan Contract's unavailability
 * 
 * @author vinodjohn
 * @created 06.09.2024
 */
public class LoanContractNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoanContractNotFoundException(UUID id) {
        super(MessageFormat.format("Loan Contract not found! (ID: {0})", id));
    }
}
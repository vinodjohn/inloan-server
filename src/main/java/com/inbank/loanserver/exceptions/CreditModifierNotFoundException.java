package com.inbank.loanserver.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Credit Modifier's unavailability
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
public class CreditModifierNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public CreditModifierNotFoundException(UUID id) {
        super(MessageFormat.format("Credit Modifier not found! (ID: {0}", id));
    }

    public CreditModifierNotFoundException(String name) {
        super(MessageFormat.format("Credit Modifier not found! (Name: {0}", name));
    }
}

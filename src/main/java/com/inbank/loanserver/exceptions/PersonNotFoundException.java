package com.inbank.loanserver.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Person's unavailability
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
public class PersonNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public PersonNotFoundException(UUID id) {
        super(MessageFormat.format("Person not found! (ID: {0})", id));
    }

    public PersonNotFoundException(String personalIdCode) {
        super(MessageFormat.format("Person not found! (Personal ID Code: {0})", personalIdCode));
    }
}

package com.inbank.loanserver.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Key Value's unavailability
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
public class KeyValueStoreNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public KeyValueStoreNotFoundException(UUID id) {
        super(MessageFormat.format("KeyValue not found! (ID: {0}", id));
    }

    public KeyValueStoreNotFoundException(String key) {
        super(MessageFormat.format("KeyValue not found! (Key: {0}", key));
    }
}

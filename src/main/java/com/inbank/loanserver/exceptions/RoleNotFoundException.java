package com.inbank.loanserver.exceptions;

import com.inbank.loanserver.models.RoleType;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Role's unavailability
 *  
 * @author vinodjohn
 * @created 02.09.2024
 */
public class RoleNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public RoleNotFoundException(UUID id) {
        super(MessageFormat.format("Role not found! (ID: {0})", id));
    }

    public RoleNotFoundException(RoleType roleType) {
        super(MessageFormat.format("Role not found! (Name: {0})", roleType.name()));
    }
}

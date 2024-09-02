package com.inbank.loanserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Token Refresh
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TokenRefreshException(UUID id) {
        super(MessageFormat.format("Token Refresh not found! (ID: {0})", id));
    }

    public TokenRefreshException(String token) {
        super(MessageFormat.format("Token Refresh not found! (Token: {0})", token));
    }

    public TokenRefreshException(String token, String message) {
        super(MessageFormat.format("Token({0}) failed! (Error: {1})", token, message));
    }
}

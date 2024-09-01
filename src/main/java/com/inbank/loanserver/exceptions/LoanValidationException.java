package com.inbank.loanserver.exceptions;

/**
 * Exception to handle validation related items
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
public class LoanValidationException extends Throwable {
    public LoanValidationException(String message) {
        super(message);
    }
}

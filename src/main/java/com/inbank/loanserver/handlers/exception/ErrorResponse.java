package com.inbank.loanserver.handlers.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Model for Error response
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private List<String> details;
}


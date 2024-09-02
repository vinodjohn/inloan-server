package com.inbank.loanserver.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Sign in Request DTO
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public record SignIn(@NotBlank(message = "{messages.constraints.invalid-personal-id}") String personalIdCode,
                     @NotBlank(message = "{messages.constraints.invalid-password}") String password) {
}

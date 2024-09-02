package com.inbank.loanserver.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Token refresh request DTO
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public record TokenRefreshRequest(@NotBlank(message = "{messages.constraints.invalid-token}") String refreshToken) {
}

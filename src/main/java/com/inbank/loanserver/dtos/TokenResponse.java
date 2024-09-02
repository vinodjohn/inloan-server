package com.inbank.loanserver.dtos;

import java.util.UUID;

/**
 * Token Response DTO
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public record TokenResponse(UUID id, String username, String role, String token, String refreshToken, String type) {
    public TokenResponse {
        if (type == null) {
            type = "Bearer";
        }
    }
}

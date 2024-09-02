package com.inbank.loanserver.dtos;

/**
 * Token Refresh Response DTO
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public record TokenRefreshResponse(String accessToken, String refreshToken, String type) {
    public TokenRefreshResponse {
        if (type == null) {
            type = "Bearer";
        }
    }
}

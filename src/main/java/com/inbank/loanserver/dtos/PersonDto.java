package com.inbank.loanserver.dtos;

import java.util.UUID;

/**
 * Person DTO
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public record PersonDto(UUID id, String fullName, String personalId, String role) {
}

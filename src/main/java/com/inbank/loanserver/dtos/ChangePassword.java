package com.inbank.loanserver.dtos;

import com.inbank.loanserver.utils.constraints.ValidChangePassword;

/**
 * Change password DTO
 *
 * @author vinodjohn
 * @created 09.09.2024
 */
@ValidChangePassword
public record ChangePassword(String oldPassword, String newPassword) {
}

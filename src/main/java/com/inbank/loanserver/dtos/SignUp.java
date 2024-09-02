package com.inbank.loanserver.dtos;

import com.inbank.loanserver.utils.constraints.ValidSignup;

/**
 * Sign up request DTO
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@ValidSignup
public record SignUp(String firstName, String lastName, String personalIdCode, String password) {
}


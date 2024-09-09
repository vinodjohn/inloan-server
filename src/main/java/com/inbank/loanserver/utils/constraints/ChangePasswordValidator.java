package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.dtos.ChangePassword;
import com.inbank.loanserver.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Change Password is valid
 *
 * @author vinodjohn
 * @created 09.09.2024
 */
@Component
public class ChangePasswordValidator implements ConstraintValidator<ValidChangePassword, ChangePassword> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidChangePassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ChangePassword changePassword, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validationService.validateChangePassword(changePassword);
            return true;
        } catch (RuntimeException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}


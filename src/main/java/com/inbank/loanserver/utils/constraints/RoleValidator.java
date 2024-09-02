package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Role is valid
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Component
public class RoleValidator implements ConstraintValidator<ValidRole, Role> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validationService.validateRole(role);
            return true;
        } catch (LoanValidationException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}

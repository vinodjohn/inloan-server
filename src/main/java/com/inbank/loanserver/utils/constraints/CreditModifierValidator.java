package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Credit Modifier is valid
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@Component
public class CreditModifierValidator implements ConstraintValidator<ValidCreditModifier, CreditModifier> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidCreditModifier constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreditModifier creditModifier, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validationService.validateCreditModifier(creditModifier);
            return true;
        } catch (LoanValidationException | CreditModifierNotFoundException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}

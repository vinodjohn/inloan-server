package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.dtos.LoanRequest;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Loan Request is valid
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Component
public class LoanRequestValidator implements ConstraintValidator<ValidLoanRequest, LoanRequest> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidLoanRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LoanRequest loanRequest, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validationService.validateLoanRequest(loanRequest);
            return true;
        } catch (LoanValidationException | KeyValueStoreNotFoundException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}


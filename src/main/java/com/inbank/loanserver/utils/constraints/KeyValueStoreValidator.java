package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if key-value store is valid
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@Component
public class KeyValueStoreValidator implements ConstraintValidator<ValidKeyValueStore, KeyValueStore> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidKeyValueStore constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(KeyValueStore keyValueStore, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validationService.validateKeyValueStore(keyValueStore);
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

package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Person is valid
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Component
public class PersonValidator implements ConstraintValidator<ValidPerson, Person> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidPerson constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validationService.validatePerson(person);
            return true;
        } catch (LoanValidationException | PersonNotFoundException | CreditModifierNotFoundException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}


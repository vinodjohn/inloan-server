package com.inbank.loanserver.utils.constraints;

import com.inbank.loanserver.dtos.SignUp;
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
 * Constraint validator to check if Signup is valid
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Component
public class SignupValidator implements ConstraintValidator<ValidSignup, SignUp> {
    @Autowired
    private ValidationService validationService;

    @Override
    public void initialize(ValidSignup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUp signup, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Person person = new Person();
            person.setFirstName(signup.firstName());
            person.setLastName(signup.lastName());
            person.setPersonalIdCode(signup.personalIdCode());
            person.setPassword(signup.password());

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

package com.inbank.loanserver.utils.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Constraint annotation for Loan Request
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Constraint(validatedBy = {LoanRequestValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLoanRequest {
    String message() default "{messages.constraints.invalid-data}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

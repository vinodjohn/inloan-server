package com.inbank.loanserver.utils.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Constraint annotation for KeyValueStore
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Constraint(validatedBy = {KeyValueStoreValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidKeyValueStore {
    String message() default "{messages.constraints.invalid-data}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

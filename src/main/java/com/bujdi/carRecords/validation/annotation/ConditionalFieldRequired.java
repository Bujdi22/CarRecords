package com.bujdi.carRecords.validation.annotation;

import com.bujdi.carRecords.validation.validator.ConditionalFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConditionalFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalFieldRequired {

    String message() default "Field is required if the other field is provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldToCheck();  // The field that is checked for presence
    String fieldToValidate(); // The field that is conditionally validated
}
package com.bujdi.carRecords.validation.annotation;

import com.bujdi.carRecords.validation.validator.UniqueConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueConstraintValidator.class)
public @interface UniqueInDatabase {

    String message() default "Value is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Specify the entity and field to check
    Class<?> entity();
    String field();
}
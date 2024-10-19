package com.bujdi.carRecords.validation.annotation;

import com.bujdi.carRecords.validation.validator.ExistsInDatabaseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsInDatabaseValidator.class)
public @interface ExistsInDatabase {

    String message() default "Value does not exist in the database";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Specify the entity and field to check
    Class<?> entity();
    String field();
}
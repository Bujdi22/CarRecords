package com.bujdi.carRecords.validation.validator;

import com.bujdi.carRecords.validation.annotation.ConditionalFieldRequired;
import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class ConditionalFieldValidator implements ConstraintValidator<ConditionalFieldRequired, Object> {

    private String fieldToCheck;
    private String fieldToValidate;

    @Override
    public void initialize(ConditionalFieldRequired constraintAnnotation) {
        this.fieldToCheck = constraintAnnotation.fieldToCheck();
        this.fieldToValidate = constraintAnnotation.fieldToValidate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // Get the values of the fields using reflection
            Field checkField = value.getClass().getDeclaredField(fieldToCheck);
            checkField.setAccessible(true);
            Object checkFieldValue = checkField.get(value);

            Field validateField = value.getClass().getDeclaredField(fieldToValidate);
            validateField.setAccessible(true);
            Object validateFieldValue = validateField.get(value);

            // If the check field has a value and the validate field is null, return false (validation fails)
            if (checkFieldValue != null && validateFieldValue == null) {
                return false; // Invalid, since the condition is violated
            }
            return true; // Valid
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // If there is any reflection error, the validation fails
            return false;
        }
    }
}
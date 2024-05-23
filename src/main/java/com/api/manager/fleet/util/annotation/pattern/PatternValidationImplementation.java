package com.api.manager.fleet.util.annotation.pattern;


import com.api.manager.fleet.util.exception.GenericException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class PatternValidationImplementation implements ConstraintValidator<PatternValidation, String> {

    private PatternType type;
    private String regexp;
    private String message;

    @Override
    public void initialize(PatternValidation constraintAnnotation) {
        this.type = constraintAnnotation.type();
        this.regexp = constraintAnnotation.regex().isEmpty() ? getBuiltInRegex(type) : constraintAnnotation.regex();
        this.message = constraintAnnotation.message(); // Store the message from the annotation
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (value == null || value.isEmpty()) {
                return true;
            }
            if (!Pattern.compile(regexp).matcher(value).matches()) {
                context.buildConstraintViolationWithTemplate(this.message)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new GenericException("Problema", HttpStatus.BAD_REQUEST);
        }
    }

    private String getBuiltInRegex(PatternType type) {
        switch (type) {
            case EMAIL:
                return "^[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+)*@[a-zA-Z0-9](?:[-a-zA-Z0-9]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[-a-zA-Z0-9]{0,61}[a-zA-Z0-9])?)*$";
            default:
                throw new IllegalArgumentException("Unsupported PatternType: " + type);
        }
    }
}

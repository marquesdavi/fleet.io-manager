package com.api.manager.fleet.util.annotation.pattern;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatternValidationImplementation.class)
public @interface PatternValidation {
    String message() default "Invalid format";
    PatternType type();
    String regex() default "";
    Class<?>[] payload() default {};
    Class<?>[] groups() default {};
}
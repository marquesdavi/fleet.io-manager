package com.api.manager.fleet.util.error;

public class ValidationError {

    private String field;
    private String message;
    private Object invalidValue;

    public ValidationError(String field, String message, Object invalidValue) {
        this.field = field;
        this.message = message;
        this.invalidValue = invalidValue;
    }

    // Getters and setters for field, message, and invalidValue (optional)
}


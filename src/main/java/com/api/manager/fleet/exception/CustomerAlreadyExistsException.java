package com.api.manager.fleet.exception;

import org.springframework.http.HttpStatus;

public class CustomerAlreadyExistsException extends RuntimeException {
    private final HttpStatus status;

    public CustomerAlreadyExistsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}


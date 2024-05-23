package com.api.manager.fleet.util.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends GenericException {
    public CustomerNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

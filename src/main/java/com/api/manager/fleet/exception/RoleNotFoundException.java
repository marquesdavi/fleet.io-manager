package com.api.manager.fleet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleNotFoundException extends ResponseStatusException {
    public RoleNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}


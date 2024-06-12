package com.api.manager.fleet.util.validation;

import com.api.manager.fleet.service.implementation.CustomerService;
import com.api.manager.fleet.exception.GenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    public void throwNotFoundException(String message) {
        logger.error(message);
        throw new GenericException(message, HttpStatus.NOT_FOUND);
    }

}

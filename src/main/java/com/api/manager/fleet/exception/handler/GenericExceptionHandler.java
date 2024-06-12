package com.api.manager.fleet.exception.handler;

import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.util.error.ValidationError;
import com.api.manager.fleet.exception.CustomerNotFoundException;
import com.api.manager.fleet.exception.GenericException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponseDTO> handleUncaughtException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DefaultResponseDTO(false, exception.getMessage()));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<DefaultResponseDTO> handleGenericException(GenericException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(new DefaultResponseDTO(false, exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public DefaultResponseDTO handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationError> errors = new ArrayList<>();
        for (ConstraintViolation constraintViolation : ex.getConstraintViolations()) {
            String fieldName = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            Object invalidValue = constraintViolation.getInvalidValue();

            errors.add(new ValidationError(fieldName, message, invalidValue));
        }

        return new DefaultResponseDTO(false, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DefaultResponseDTO handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return new DefaultResponseDTO(false, errorMap);
    }

    public static void throwNotFoundException(Logger logger, String message) {
        logger.error(message);
        throw new GenericException(message, HttpStatus.NOT_FOUND);
    }

    public static void throwConflictException(Logger logger, String message) {
        logger.error(message);
        throw new GenericException(message, HttpStatus.CONFLICT);
    }
}


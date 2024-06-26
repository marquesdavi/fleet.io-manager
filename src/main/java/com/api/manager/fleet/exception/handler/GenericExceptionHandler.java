package com.api.manager.fleet.exception.handler;

import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.util.error.ValidationError;
import com.api.manager.fleet.exception.NotFoundException;
import com.api.manager.fleet.exception.GenericException;
import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(NotFoundException exception) {
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ValidationError> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationError> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            String fieldName = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            Object invalidValue = constraintViolation.getInvalidValue();
            errors.add(new ValidationError(fieldName, message, invalidValue));
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return errorMap;
    }
}


package com.api.manager.fleet.util.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Access Denied");
        responseBody.put("message", "You do not have permission to perform this action.");
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
    }
}
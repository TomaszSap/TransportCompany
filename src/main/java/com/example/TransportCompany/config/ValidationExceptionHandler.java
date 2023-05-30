package com.example.TransportCompany.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(javax.validation.ConstraintViolationException ex) {
        String errorMessage = "Incorrect values!!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage+" "+ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
    }
}
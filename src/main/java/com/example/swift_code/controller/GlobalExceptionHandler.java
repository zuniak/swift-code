package com.example.swift_code.controller;

import com.example.swift_code.exceptions.BankSwiftDuplicateException;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.exceptions.BankSwiftValidationException;
import com.example.swift_code.exceptions.NoCodesFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BankSwiftValidationException.class)
    public ResponseEntity<Map<String, String>> handleBankSwiftValidationException(BankSwiftValidationException exception) {
        String message = exception.getMessage();

        String violationMessage = exception.getViolationMessage();

        String responseMessage;

        if (message.isEmpty() && violationMessage.isEmpty()) {
            responseMessage = "BankSwift validation failed";
        } else if (message.isEmpty()) {
            responseMessage = violationMessage;
        } else if (violationMessage.isEmpty()) {
            responseMessage = message;
        } else {
            responseMessage = message + ": " + violationMessage;
        }
        return ResponseEntity.badRequest().body(Map.of("message", responseMessage));
    }

    @ExceptionHandler(BankSwiftNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBankSwiftNotFoundException(BankSwiftNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(BankSwiftDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleBankSwiftDuplicateException(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(NoCodesFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoCodesFoundException(NoCodesFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
    }
}

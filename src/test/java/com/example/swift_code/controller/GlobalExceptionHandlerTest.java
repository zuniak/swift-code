package com.example.swift_code.controller;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.exceptions.BankSwiftDuplicateException;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.exceptions.BankSwiftValidationException;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleBankSwiftValidationException() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<BankSwiftDto> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Violation message 1.");

        BankSwiftValidationException exception = new BankSwiftValidationException("Validation failed", Set.of(violation));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleBankSwiftValidationException(exception);

        Map<String, String> expectedResponse = Map.of("message", "Validation failed: Violation message 1.");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void handleBankSwiftNotFoundException() {
        String message = "Bank Swift code not found.";
        BankSwiftNotFoundException exception = new BankSwiftNotFoundException(message);

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleBankSwiftNotFoundException(exception);

        Map<String, String> expectedResponse = Map.of("message", message);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void handleBankSwiftDuplicateException(){
        String message = "Bank Swift code already exists.";
        BankSwiftDuplicateException exception = new BankSwiftDuplicateException(message);

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleBankSwiftDuplicateException(exception);

        Map<String, String> expectedResponse = Map.of("message", message);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}

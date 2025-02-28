package com.example.swift_code.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class BankSwiftValidationException extends RuntimeException {
    private String violationMessage;
    public <T> BankSwiftValidationException(String message, Set<ConstraintViolation<T>> violations) {
        super(message);
        setViolationMessage(violations);
    }

    private <T> void setViolationMessage(Set<ConstraintViolation<T>> violations) {
        violationMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" "));
        }
}

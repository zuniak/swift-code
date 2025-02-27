package com.example.swift_code.exceptions;

import com.example.swift_code.dto.BankSwiftDto;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

@Getter
public class BankSwiftValidationException extends RuntimeException {
    Set<ConstraintViolation<BankSwiftDto>> violations;
    public BankSwiftValidationException(String message, Set<ConstraintViolation<BankSwiftDto>> violations) {
        super(message);
        this.violations = violations;
    }
}

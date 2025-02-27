package com.example.swift_code.exceptions;

public class BankSwiftNotFoundException extends RuntimeException {
    public BankSwiftNotFoundException(String message) {
        super(message);
    }
}

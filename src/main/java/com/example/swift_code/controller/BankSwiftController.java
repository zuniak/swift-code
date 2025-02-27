package com.example.swift_code.controller;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.exceptions.BankSwiftValidationException;
import com.example.swift_code.service.BankSwiftService;
import com.example.swift_code.validationgroups.BankBranch;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/swift-codes")
@AllArgsConstructor
public class BankSwiftController {

    private final BankSwiftService service;
    private final Validator validator;

    @PostMapping
    public ResponseEntity<Map<String, String>> addBankSwift(@RequestBody BankSwiftDto bankSwiftDto) {
        Set<ConstraintViolation<BankSwiftDto>> violations = validator.validate(bankSwiftDto, BankBranch.class);
        if (!violations.isEmpty()) {
            throw new BankSwiftValidationException("SWIFT code validation failed", violations);
        }
        service.addBankSwift(bankSwiftDto);

        return ResponseEntity.ok(Map.of("message", "SWIFT code data added successfully."));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<Map<String, String>> deleteBankSwift(@PathVariable String swiftCode) {
        service.deleteBankSwift(swiftCode);
        return ResponseEntity.ok(Map.of("message", "SWIFT code data deleted successfully."));
    }
}

package com.example.swift_code.controller;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.exceptions.BankSwiftValidationException;
import com.example.swift_code.service.BankSwiftService;
import com.example.swift_code.validationgroups.BankBranch;
import com.example.swift_code.validationgroups.BankHeadquarter;
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

    @GetMapping("/{swiftCode}")
    public ResponseEntity<BankSwiftDto> getBankSwift(@PathVariable String swiftCode) {
        BankSwiftDto bankSwiftDto = service.getBankSwiftDto(swiftCode);
        if (bankSwiftDto.isHeadquarter()) {
            validateDto(bankSwiftDto, BankHeadquarter.class);
        } else {
            validateDto(bankSwiftDto, BankBranch.class);
        }
        return ResponseEntity.ok(bankSwiftDto);
    }

    private void validateDto(BankSwiftDto bankSwiftDto, Class<?> validationGroup) {
        Set<ConstraintViolation<BankSwiftDto>> violations = validator.validate(bankSwiftDto, validationGroup);
        if (!violations.isEmpty()) {
            throw new BankSwiftValidationException("Invalid SWIFT code DTO format", violations);
        }
    }
}

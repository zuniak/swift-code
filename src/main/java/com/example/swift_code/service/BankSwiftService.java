package com.example.swift_code.service;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.exceptions.BankSwiftDuplicateException;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.mapper.BankSwiftMapper;
import com.example.swift_code.repository.BankSwiftRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankSwiftService {
    private final BankSwiftRepository repository;
    private final BankSwiftMapper mapper;

    public void addBankSwift(BankSwiftDto bankSwiftDto) {
        if (repository.existsById(bankSwiftDto.getSwiftCode())) {
            throw new BankSwiftDuplicateException("SWIFT code already exists: " + bankSwiftDto.getSwiftCode());
        }
        repository.saveAndFlush(mapper.toEntity(bankSwiftDto));
    }

    public void deleteBankSwift(String swiftCode) {
        if (!repository.existsById(swiftCode)) {
            throw new BankSwiftNotFoundException("SWIFT code: " + swiftCode + " not found.");
        }
        repository.deleteById(swiftCode);
    }
}

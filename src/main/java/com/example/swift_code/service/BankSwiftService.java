package com.example.swift_code.service;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.entity.BankSwift;
import com.example.swift_code.exceptions.BankSwiftDuplicateException;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.mapper.BankSwiftMapper;
import com.example.swift_code.repository.BankSwiftRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public BankSwiftDto getBankSwiftDto(String swiftcode) {
        Optional<BankSwift> bankSwiftOpt = repository.findById(swiftcode);
        if (bankSwiftOpt.isPresent()) {
            BankSwift bankSwift = bankSwiftOpt.get();

            if (bankSwift.isHeadquarter()){
                return getHeadquarterDto(bankSwift);
            }
            else {
                return mapper.toDTOBranch(bankSwift);
            }
        }
        throw new BankSwiftNotFoundException("SWIFT code: " + swiftcode + " not found.");
    }

    private BankSwiftDto getHeadquarterDto(BankSwift headquarter) {
        BankSwiftDto headquarterDto = mapper.toDTOHeadquarter(headquarter);
        List<BankSwiftDto> branches = getBranches(headquarter.getSwiftCode());
        headquarterDto.setBranches(branches);
        return headquarterDto;
    }

    private List<BankSwiftDto> getBranches(String swiftcode) {
        String baseCode = swiftcode.substring(0, 8);
        return repository.findBySwiftCodeStartingWithAndSwiftCodeNot(baseCode, swiftcode)
                .stream()
                .map(mapper::toDTOReduced)
                .collect(Collectors.toList());
    }
}

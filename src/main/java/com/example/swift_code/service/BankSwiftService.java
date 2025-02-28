package com.example.swift_code.service;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.dto.CountryBankSwiftDto;
import com.example.swift_code.entity.BankSwift;
import com.example.swift_code.exceptions.BankSwiftDuplicateException;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.exceptions.BankSwiftValidationException;
import com.example.swift_code.exceptions.NoCodesFoundException;
import com.example.swift_code.mapper.BankSwiftMapper;
import com.example.swift_code.repository.BankSwiftRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;

@Service
@AllArgsConstructor
public class BankSwiftService {
    private final BankSwiftRepository repository;
    private final BankSwiftMapper mapper;
    private final Validator validator;

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
            } else {
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
                .toList();
    }

    public CountryBankSwiftDto getAllCountryCodes(String countryIS02) {
        List<BankSwift> branches = repository.findAllByCountryIS02(countryIS02);
        if (branches.isEmpty()){
            throw new NoCodesFoundException("No SWIFT codes found for country: " + countryIS02);
        }
        String countryName = branches.get(0).getCountryName();
        List<BankSwiftDto> branchesDto = branches.stream().map(mapper::toDTOReduced).toList();

        return new CountryBankSwiftDto(countryIS02, countryName, branchesDto);
    }

    public void downloadAndSaveBankSwiftData() throws IOException {
        URL url = new URL("https://docs.google.com/spreadsheets/d/1iFFqsu_xruvVKzXAadAAlDBpIuU51v-pfIEU5HeGa8w/gviz/tq?tqx=out:csv&sheet=Sheet1");
        InputStream inputStream = url.openStream();

        List<BankSwift> bankSwifts = parseCsvToBankSwift(inputStream);

        bankSwifts.forEach(this::validate);

        repository.saveAll(bankSwifts);
    }

    private List<BankSwift> parseCsvToBankSwift(InputStream inputStream){
        try (Reader reader = new InputStreamReader(inputStream)) {
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> rows = csvReader.readAll();
            return rows.stream().skip(1).map(this::mapToEntity).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV file", e);
        }
    }

    private BankSwift mapToEntity(String[] row){
        BankSwift bankSwift = new BankSwift();
        bankSwift.setCountryIS02(row[0]);
        bankSwift.setSwiftCode(row[1]);
        bankSwift.setBankName(row[3]);
        bankSwift.setAddress(row[4]);
        bankSwift.setCountryName(row[6]);
        bankSwift.setHeadquarter(bankSwift.getSwiftCode().endsWith("XXX"));
        return bankSwift;
    }

    private void validate(BankSwift bankSwift){
        Set<ConstraintViolation<BankSwift>> violations = validator.validate(bankSwift);
        if (!violations.isEmpty()) {
            throw new BankSwiftValidationException("Invalid SWIFT code format", violations);
        }
    }
}

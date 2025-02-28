package com.example.swift_code.controller;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.dto.CountryBankSwiftDto;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.exceptions.BankSwiftValidationException;
import com.example.swift_code.exceptions.NoCodesFoundException;
import com.example.swift_code.service.BankSwiftService;
import com.example.swift_code.validationgroups.BankBranch;
import com.example.swift_code.validationgroups.BankHeadquarter;
import com.example.swift_code.validationgroups.BankInfoReduced;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankSwiftControllerTest {
    @Mock
    BankSwiftService service;

    @Mock
    Validator validator;

    @InjectMocks
    BankSwiftController controller;

    @BeforeEach
    void resetMocks() {
        reset(service, validator);
    }

    @Test
    void addBankSwift_whenValidInput_shouldReturnOk() {
        BankSwiftDto bankSwiftDto = new BankSwiftDto();

        when(validator.validate(bankSwiftDto, BankBranch.class)).thenReturn(Set.of());
        ResponseEntity<Map<String, String>> response = controller.addBankSwift(bankSwiftDto);

        Map<String, String> expectedResponse = Map.of("message", "SWIFT code data added successfully.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(validator, times(1)).validate(bankSwiftDto, BankBranch.class);
        verify(service, times(1)).addBankSwift(bankSwiftDto);
    }

    @Test
    void addBankSwift_whenInvalidInput_shouldThrowBankSwiftValidationException() {
        BankSwiftDto bankSwiftDto = new BankSwiftDto();

        @SuppressWarnings("unchecked")
        ConstraintViolation<BankSwiftDto> violation = mock(ConstraintViolation.class);
        when(validator.validate(bankSwiftDto, BankBranch.class)).thenReturn(Set.of(violation));

        assertThrows(BankSwiftValidationException.class, () -> controller.addBankSwift(bankSwiftDto));

        verify(validator, times(1)).validate(bankSwiftDto, BankBranch.class);
        verify(service, never()).addBankSwift(bankSwiftDto);
    }

    @Test
    void deleteBankSwift_whenValidInput_shouldReturnOk() {
        String swiftCode = "TESTXXX";

        ResponseEntity<Map<String, String>> response = controller.deleteBankSwift(swiftCode);

        Map<String, String> expectedResponse = Map.of("message", "SWIFT code data deleted successfully.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(service, times(1)).deleteBankSwift(swiftCode);
    }

    @Test
    void deleteBankSwift_whenInvalidInput_shouldThrowBankSwiftNotFoundException() {
        String swiftCode = "INVALID";

        String message = "Bank Swift code not found.";
        doThrow(new BankSwiftNotFoundException(message)).when(service).deleteBankSwift(swiftCode);
        BankSwiftNotFoundException exception = assertThrows(BankSwiftNotFoundException.class, () -> controller.deleteBankSwift(swiftCode));

        assertEquals(message, exception.getMessage());

        verify(service, times(1)).deleteBankSwift(swiftCode);
    }

    @Test
    void getBankSwift_whenValidHeadquarter_shouldReturnOk() {
        String swiftCode = "12345678XXX";
        BankSwiftDto bankSwiftDto = new BankSwiftDto();
        bankSwiftDto.setHeadquarter(true);

        when(service.getBankSwiftDto(swiftCode)).thenReturn(bankSwiftDto);
        when(validator.validate(bankSwiftDto, BankHeadquarter.class)).thenReturn(Set.of());

        ResponseEntity<BankSwiftDto> response = controller.getBankSwift(swiftCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankSwiftDto, response.getBody());

        verify(service, times(1)).getBankSwiftDto(swiftCode);
        verify(validator, times(1)).validate(bankSwiftDto, BankHeadquarter.class);
    }

    @Test
    void getBankSwift_whenInvalidHeadquarter_shouldThrowBankSwiftValidationException() {
        String swiftCode = "12345678XXX";
        BankSwiftDto bankSwiftDto = new BankSwiftDto();
        bankSwiftDto.setHeadquarter(true);

        when(service.getBankSwiftDto(swiftCode)).thenReturn(bankSwiftDto);

        @SuppressWarnings("unchecked")
        ConstraintViolation<BankSwiftDto> violation = mock(ConstraintViolation.class);
        when(validator.validate(bankSwiftDto, BankHeadquarter.class)).thenReturn(Set.of(violation));

        assertThrows(BankSwiftValidationException.class, () -> controller.getBankSwift(swiftCode));

        verify(service, times(1)).getBankSwiftDto(swiftCode);
        verify(validator, times(1)).validate(bankSwiftDto, BankHeadquarter.class);
    }

    @Test
    void getBankSwift_whenValidBranch_shouldReturnOk() {
        String swiftCode = "12345678XXX";
        BankSwiftDto bankSwiftDto = new BankSwiftDto();
        bankSwiftDto.setHeadquarter(false);

        when(service.getBankSwiftDto(swiftCode)).thenReturn(bankSwiftDto);
        when(validator.validate(bankSwiftDto, BankBranch.class)).thenReturn(Set.of());

        ResponseEntity<BankSwiftDto> response = controller.getBankSwift(swiftCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankSwiftDto, response.getBody());

        verify(service, times(1)).getBankSwiftDto(swiftCode);
        verify(validator, times(1)).validate(bankSwiftDto, BankBranch.class);
    }

    @Test
    void getBankSwift_whenInvalidBranch_shouldThrowBankSwiftValidationException() {
        String swiftCode = "12345678XXX";
        BankSwiftDto bankSwiftDto = new BankSwiftDto();
        bankSwiftDto.setHeadquarter(false);

        when(service.getBankSwiftDto(swiftCode)).thenReturn(bankSwiftDto);

        @SuppressWarnings("unchecked")
        ConstraintViolation<BankSwiftDto> violation = mock(ConstraintViolation.class);
        when(validator.validate(bankSwiftDto, BankBranch.class)).thenReturn(Set.of(violation));

        assertThrows(BankSwiftValidationException.class, () -> controller.getBankSwift(swiftCode));

        verify(service, times(1)).getBankSwiftDto(swiftCode);
        verify(validator, times(1)).validate(bankSwiftDto, BankBranch.class);
    }

    @Test
    void getAllCountryCodes_whenValidCountryBankSwiftDto_shouldReturnOk() {
        String countryIS02 = "TT";
        CountryBankSwiftDto countryDto = new CountryBankSwiftDto("", "", List.of());
        BankSwiftDto branchDto = new BankSwiftDto();
        countryDto.setBranches(List.of(branchDto));

        when(service.getAllCountryCodes(countryIS02)).thenReturn(countryDto);
        when(validator.validate(branchDto, BankInfoReduced.class)).thenReturn(Set.of());

        controller.getAllCountryCodes(countryIS02);

        verify(service, times(1)).getAllCountryCodes(countryIS02);
        verify(validator, times(1)).validate(branchDto, BankInfoReduced.class);
    }

    @Test
    void getAllCountryCodes_whenNoCodesFound_shouldThrowsNotFoundException() {
        String countryIS02 = "TT";
        when(service.getAllCountryCodes(countryIS02)).thenThrow(new NoCodesFoundException("No codes found."));

        assertThrows(NoCodesFoundException.class, () -> controller.getAllCountryCodes(countryIS02));
    }

    @Test
    void getAllCountryCodes_whenInvalidBankSwiftDto_shouldThrowBankSwiftValidationException() {
        String countryIS02 = "TT";
        CountryBankSwiftDto countryDto = new CountryBankSwiftDto("", "", List.of());
        BankSwiftDto branchDto = new BankSwiftDto();
        countryDto.setBranches(List.of(branchDto));

        when(service.getAllCountryCodes(countryIS02)).thenReturn(countryDto);
        @SuppressWarnings("unchecked")
        ConstraintViolation<BankSwiftDto> violation = mock(ConstraintViolation.class);
        when(validator.validate(branchDto, BankInfoReduced.class)).thenReturn(Set.of(violation));

        assertThrows(BankSwiftValidationException.class, () -> controller.getAllCountryCodes(countryIS02));

        verify(service, times(1)).getAllCountryCodes(countryIS02);
        verify(validator, times(1)).validate(any(), any());
    }
}
package com.example.swift_code.service;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.entity.BankSwift;
import com.example.swift_code.exceptions.BankSwiftDuplicateException;
import com.example.swift_code.exceptions.BankSwiftNotFoundException;
import com.example.swift_code.mapper.BankSwiftMapper;
import com.example.swift_code.repository.BankSwiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankSwiftServiceTest {

    @Mock
    BankSwiftRepository repository;

    @Mock
    BankSwiftMapper mapper;

    @InjectMocks
    BankSwiftService service;

    @BeforeEach
    void resetMocks() {
        reset(repository, mapper);
    }

    @Test
    void addBankSwift_shouldSaveData() {
        BankSwiftDto bankSwiftDto = new BankSwiftDto();

        when(repository.existsById(bankSwiftDto.getSwiftCode())).thenReturn(false);
        when(mapper.toEntity(bankSwiftDto)).thenReturn(new BankSwift());

        service.addBankSwift(bankSwiftDto);

        verify(repository, times(1)).existsById(bankSwiftDto.getSwiftCode());
        verify(mapper, times(1)).toEntity(bankSwiftDto);
        verify(repository, times(1)).saveAndFlush(any(BankSwift.class));
    }

    @Test
    void addBankSwift_whenIdAlreadyExists_shouldThrowBankSwiftDuplicateException() {
        BankSwiftDto bankSwiftDto = new BankSwiftDto();
        bankSwiftDto.setSwiftCode("TESTXXX");

        when(repository.existsById(bankSwiftDto.getSwiftCode())).thenReturn(true);

        BankSwiftDuplicateException exception = assertThrows(BankSwiftDuplicateException.class, () -> service.addBankSwift(bankSwiftDto));
        assertEquals("SWIFT code already exists: " + bankSwiftDto.getSwiftCode(), exception.getMessage());

        verify(repository, times(1)).existsById(bankSwiftDto.getSwiftCode());
        verify(mapper, never()).toEntity(bankSwiftDto);
        verify(repository, never()).saveAndFlush(any(BankSwift.class));
    }

    @Test
    void deleteBankSwift_whenSwiftCodeFound_shouldDeleteData() {
        String swiftCode = "TESTXXX";

        when(repository.existsById(swiftCode)).thenReturn(true);

        service.deleteBankSwift(swiftCode);

        verify(repository, times(1)).existsById(swiftCode);
        verify(repository, times(1)).deleteById(swiftCode);
    }

    @Test
    void deleteBankSwift_whenSwiftCodeNotFound_shouldThrowBankSwiftNotFoundException() {
        String swiftCode = "TESTXXX";

        when(repository.existsById(swiftCode)).thenReturn(false);

        BankSwiftNotFoundException exception = assertThrows(BankSwiftNotFoundException.class, () -> service.deleteBankSwift(swiftCode));
        assertEquals("SWIFT code: " + swiftCode + " not found.", exception.getMessage());

        verify(repository, times(1)).existsById(swiftCode);
        verify(repository, never()).deleteById(swiftCode);
    }
}
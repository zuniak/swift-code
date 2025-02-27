package com.example.swift_code.mapper;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.entity.BankSwift;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankSwiftMapper {

    BankSwift toEntity(BankSwiftDto bankSwiftDto);
}

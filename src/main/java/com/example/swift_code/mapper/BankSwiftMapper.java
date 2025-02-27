package com.example.swift_code.mapper;

import com.example.swift_code.dto.BankSwiftDto;
import com.example.swift_code.entity.BankSwift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankSwiftMapper {

    BankSwift toEntity(BankSwiftDto bankSwiftDto);

    @Mapping(target = "branches", ignore = true)
    BankSwiftDto toDTOHeadquarter(BankSwift code);

    @Mapping(target = "branches", ignore = true)
    BankSwiftDto toDTOBranch(BankSwift code);

    @Mapping(target = "countryName", ignore = true)
    @Mapping(target = "branches", ignore = true)
    BankSwiftDto toDTOReduced(BankSwift code);
}

package com.example.swift_code.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

@JsonPropertyOrder({"countryIS02", "countryName", "branches"})
public class CountryBankSwiftDto {

    private String countryIS02;

    private String countryName;

    private List<BankSwiftDto> branches;
}

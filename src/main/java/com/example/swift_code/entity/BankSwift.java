package com.example.swift_code.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankSwift {
    @Id
    private String swiftCode;

    @NotBlank
    private String countryISO2;

    @NotBlank
    private String countryName;

    @NotBlank
    private String bankName;

    @NotBlank
    private String address;

    @NotNull
    private boolean isHeadquarter;
}

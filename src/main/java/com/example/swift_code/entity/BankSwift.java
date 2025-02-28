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
    @NotBlank(message = "SWIFT code is mandatory")
    private String swiftCode;

    @NotBlank(message = "Country ISO2 code is mandatory")
    private String countryIS02;

    @NotBlank(message = "Country name is mandatory")
    private String countryName;

    @NotBlank(message = "Bank name is mandatory")
    private String bankName;

    private String address;

    @NotNull(message = "Headquarter flag is mandatory")
    private boolean isHeadquarter;
}

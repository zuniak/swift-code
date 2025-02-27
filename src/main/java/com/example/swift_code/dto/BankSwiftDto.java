package com.example.swift_code.dto;

import com.example.swift_code.validationgroups.BankBranch;
import com.example.swift_code.validationgroups.BankHeadquarter;
import com.example.swift_code.validationgroups.BankInfoReduced;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BankSwiftDto {
    @NotBlank(groups = {BankBranch.class, BankHeadquarter.class, BankInfoReduced.class}, message = "Address is mandatory.")
    private String address;

    @NotBlank(groups = {BankBranch.class, BankHeadquarter.class, BankInfoReduced.class}, message = "Bank name is mandatory.")
    private String bankName;

    @NotBlank(groups = {BankBranch.class, BankHeadquarter.class, BankInfoReduced.class}, message = "Country ISO2 is mandatory.")
    private String countryISO2;

    @NotBlank(groups = {BankBranch.class, BankHeadquarter.class}, message = "Country name is mandatory.")
    @Null(groups = BankInfoReduced.class, message = "Country name must be null.")
    private String countryName;

    @NotNull(groups = {BankBranch.class, BankHeadquarter.class, BankInfoReduced.class}, message = "Headquarter is mandatory.")
    private boolean isHeadquarter;

    @NotBlank(groups = {BankBranch.class, BankHeadquarter.class, BankInfoReduced.class}, message = "SWIFT code is mandatory.")
    private String swiftCode;

    @Null(groups = {BankBranch.class, BankInfoReduced.class}, message = "Branches must be null")
    @Size(groups = BankHeadquarter.class)
    private List<BankSwiftDto> branches;
}

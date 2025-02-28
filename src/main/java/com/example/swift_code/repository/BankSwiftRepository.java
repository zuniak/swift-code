package com.example.swift_code.repository;

import com.example.swift_code.entity.BankSwift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankSwiftRepository extends JpaRepository<BankSwift, String> {
    List<BankSwift> findBySwiftCodeStartingWithAndSwiftCodeNot(String baseCode, String excludedCode);
    List<BankSwift> findAllByCountryIS02(String countryIS02);
}
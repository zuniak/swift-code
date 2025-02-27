package com.example.swift_code.repository;

import com.example.swift_code.entity.BankSwift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankSwiftRepository extends JpaRepository<BankSwift, String> {
}

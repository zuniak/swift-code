package com.example.swift_code.repository;

import com.example.swift_code.entity.BankSwift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BankSwiftRepositoryTest {

        @Autowired
        BankSwiftRepository repository;

        @Autowired
        TestEntityManager entityManager;

        private BankSwift headquarter;
        private BankSwift branch1;
        private BankSwift branch2;
        private BankSwift notBranch;

        @BeforeEach
        public void setUp(){
             headquarter = new BankSwift(
                    "TESTXXX",
                    "TT",
                    "Test Country",
                    "Test Bank",
                    "Test address headquarter",
                    true);
            entityManager.persist(headquarter);

             branch1 = new BankSwift(
                    "TEST001",
                    "TT",
                    "Test Country",
                    "Test Bank",
                    "Test address branch 1",
                    false);
            entityManager.persist(branch1);

             branch2 = new BankSwift(
                    "TEST002",
                    "TT",
                    "Test Country",
                    "Test Bank",
                    "Test address branch 2",
                    false);
            entityManager.persist(branch2);

             notBranch = new BankSwift(
                    "ABCXXX",
                    "TT",
                    "Test Country",
                    "Test Bank",
                    "Test address branch 3",
                    true);
            entityManager.persist(notBranch);
        }

        @Test
        public void findBySwiftCodeStartingWithAndSwiftCodeNot(){
            List<BankSwift> branches = repository.findBySwiftCodeStartingWithAndSwiftCodeNot("TEST", "TESTXXX");
            assertEquals(2, branches.size());

            assertTrue(branches.contains(branch1));
            assertTrue(branches.contains(branch2));
            assertFalse(branches.contains(headquarter));
            assertFalse(branches.contains(notBranch));
        }
}

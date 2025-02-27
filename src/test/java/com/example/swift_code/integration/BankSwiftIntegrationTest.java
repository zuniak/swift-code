package com.example.swift_code.integration;

import com.example.swift_code.entity.BankSwift;
import com.example.swift_code.repository.BankSwiftRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BankSwiftIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BankSwiftRepository repository;

    @Test
    public void addBankSwift_whenValidInput_shouldReturnOk() throws Exception {
        Map<String, String> input = Map.of("address", "Test address",
                "bankName", "Test Bank",
                "countryISO2", "TT",
                "countryName", "Test Country",
                "isHeadquarter", "true",
                "swiftCode", "TESTXXX");
        String jsonInput = new JSONObject(input).toString();

        Map<String, String> expectedResponse = Map.of("message", "SWIFT code data added successfully.");
        String jsonResponse = new JSONObject(expectedResponse).toString();

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        assertTrue(repository.existsById("TESTXXX"));
    }

    @Test
    public void addBankSwift_whenInvalidInput_shouldReturnBadRequest() throws Exception {
        Map<String, String> input = Map.of("address", "",
                "bankName", "Test Bank",
                "countryISO2", "TT",
                "countryName", "Test Country",
                "isHeadquarter", "true",
                "swiftCode", "TESTXXX");
        String jsonInput = new JSONObject(input).toString();

        Map<String, String> expectedResponse = Map.of("message", "SWIFT code validation failed:  Address is mandatory.");
        String jsonResponse = new JSONObject(expectedResponse).toString();

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonResponse));

        assertFalse(repository.existsById("TESTXXX"));
    }

    @Test
    public void addBankSwift_whenIdAlreadyExists_shouldReturnConflict() throws Exception {
        BankSwift oldBankSwift = new BankSwift("TESTXXX", "TT", "Test Country", "Old Test Name", "Test address", true);
        repository.save(oldBankSwift);
        assertTrue(repository.existsById("TESTXXX"));

        Map<String, String> input = Map.of("address", "Test address",
                "bankName", "New Test Name",
                "countryISO2", "TT",
                "countryName", "Test Country",
                "isHeadquarter", "true",
                "swiftCode", "TESTXXX");
        String jsonInput = new JSONObject(input).toString();

        Map<String, String> expectedResponse = Map.of("message", "SWIFT code already exists: TESTXXX");
        String jsonResponse = new JSONObject(expectedResponse).toString();

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isConflict())
                .andExpect(content().json(jsonResponse));

        assertTrue(repository.findById("TESTXXX").isPresent());
        assertEquals(oldBankSwift.getBankName(), repository.findById("TESTXXX").get().getBankName());
    }

    @Test
    public void deleteBankSwift_whenBankSwiftExist_shouldReturnOk() throws Exception {
        BankSwift bankSwift = new BankSwift("TESTXXX", "TT", "Test country", "Test name", "Test address", true);
        repository.save(bankSwift);
        assertTrue(repository.existsById("TESTXXX"));

        Map<String, String> expectedResponse = Map.of("message", "SWIFT code data deleted successfully.");

        String jsonResponse = new JSONObject(expectedResponse).toString();

        mockMvc.perform(delete("/v1/swift-codes/TESTXXX"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        assertFalse(repository.existsById("TESTXXX"));
    }

    @Test
    public void deleteBankSwift_whenBankSwiftNotExist_shouldReturnNotFound() throws Exception {
        Map<String, String> expectedResponse = Map.of("message", "SWIFT code: INVALID not found.");
        String jsonResponse = new JSONObject(expectedResponse).toString();

        mockMvc.perform(delete("/v1/swift-codes/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonResponse));
    }
}

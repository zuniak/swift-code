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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
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
                "countryIS02", "TT",
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
                "countryIS02", "TT",
                "countryName", "Test Country",
                "isHeadquarter", "true",
                "swiftCode", "TESTXXX");
        String jsonInput = new JSONObject(input).toString();

        Map<String, String> expectedResponse = Map.of("message", "Invalid input SWIFT code DTO format BankBranch: Address is mandatory.");
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
                "countryIS02", "TT",
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

    private void setUpRepository() {
        BankSwift headquarter = new BankSwift(
                "12345678XXX",
                "TT",
                "Test Country",
                "Test Bank",
                "Test address headquarter",
                true);
        repository.save(headquarter);

        BankSwift branch1 = new BankSwift(
                "12345678001",
                "TT",
                "Test Country",
                "Test Bank",
                "Test address branch 1",
                false);
        repository.save(branch1);

        BankSwift branch2 = new BankSwift(
                "12345678002",
                "TT",
                "Test Country",
                "Test Bank",
                "Test address branch 2",
                false);
        repository.save(branch2);

        BankSwift other = new BankSwift(
                "AAAAAAAA001",
                "AA",
                "Test Country",
                "Test Bank",
                "Test address branch 1",
                false);
        repository.save(other);
    }

    @Test
    public void getBankSwift_whenHeadquarter_shouldReturnOk() throws Exception {
        setUpRepository();
        String swiftCode = "12345678XXX";
        String expectedResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/headquarter_integration_test.json")));
        mockMvc.perform(get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getBankSwift_whenBranch_shouldReturnOk() throws Exception {
        setUpRepository();
        String swiftCode = "12345678001";
        String expectedResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/branch_integration_test.json")));
        mockMvc.perform(get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getAllCountryCodes_whenValidInput_shouldReturnOk() throws Exception {
        setUpRepository();
        String countryIS02 = "TT";
        String expectedResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/country_integration_test.json")));
        mockMvc.perform(get("/v1/swift-codes/country/" + countryIS02))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getAllCountryCodes_whenNoCodesFound_shouldReturnNotFound() throws Exception {
        setUpRepository();
        String countryIS02 = "XX";
        Map<String, String> expectedResponse = Map.of("message", "No SWIFT codes found for country: XX");
        String jsonResponse = new JSONObject(expectedResponse).toString();

        mockMvc.perform(get("/v1/swift-codes/country/" + countryIS02))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonResponse));
    }
}

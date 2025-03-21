package com.api.v2.medical_slots;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedicalSlotCompletionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testSuccessfulCompletion() throws Exception {
        String licenseNumber = "12345678";
        String state = "CA";
        String medicalSlotId = "";
        String url = "/api/v2/medical-slots/%s/%s/%s/completion".formatted(licenseNumber, state, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void testUnSuccessfulCompletionForPastCompletion() throws Exception {
        String licenseNumber = "12345678";
        String state = "CA";
        String medicalSlotId = "";
        String url = "/api/v2/medical-slots/%s/%s/%s/completion".formatted(licenseNumber, state, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    void testUnsuccessfulCompletionForNotFoundMedicalLicenseNumber() throws Exception {
        String licenseNumber = "12345677";
        String state = "CA";
        String medicalSlotId = "";
        String url = "/api/v2/medical-slots/%s/%s/%s/completion".formatted(licenseNumber, state, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(4)
    void testUnsuccessfulCompletionForNotFoundMedicalSlotId() throws Exception {
        String licenseNumber = "12345678";
        String state = "CA";
        String medicalSlotId = "";
        String url = "/api/v2/medical-slots/%s/%s/%s/completion".formatted(licenseNumber, state, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

}

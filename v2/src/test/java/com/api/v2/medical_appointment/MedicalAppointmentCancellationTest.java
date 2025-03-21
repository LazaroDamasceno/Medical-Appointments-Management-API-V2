package com.api.v2.medical_appointment;

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
class MedicalAppointmentCancellationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testSuccessfulCancellation() throws Exception {
        String customerId = "";
        String medicalSlotId = "";
        String url = "/api/v2/medical-appointments/%s/%s/cancellation".formatted(customerId, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void testUnsuccessfulCancellationForWrongCustomerId() throws Exception {
        String customerId = "";
        String medicalSlotId = "";
        String url = "/api/v2/medical-appointments/%s/%s/cancellation".formatted(customerId, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    void testUnsuccessfulCancellationForWrongMedicalSlotId() throws Exception {
        String customerId = "";
        String medicalSlotId = "";
        String url = "/api/v2/medical-appointments/%s/%s/cancellation".formatted(customerId, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(4)
    void testUnsuccessfulCancellationForPastCancellation() throws Exception {
        String customerId = "";
        String medicalSlotId = "";
        String url = "/api/v2/medical-appointments/%s/%s/cancellation".formatted(customerId, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    void testUnsuccessfulCancellationForPastCompletion() throws Exception {
        String customerId = "";
        String medicalSlotId = "";
        String url = "/api/v2/medical-appointments/%s/%s/cancellation".formatted(customerId, medicalSlotId);
        mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

}

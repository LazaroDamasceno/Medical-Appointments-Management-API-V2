package com.api.v2;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicalSlotRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    MedicalSlotRegistrationDto registrationDto = new MedicalSlotRegistrationDto(
            new MedicalLicenseNumber("12345678", "CA", "US"),
            LocalDateTime.parse("2024-12-12T12:30:30")
    );

    @Test
    @Order(1)
    void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v2/medical-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto))
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void testUnsuccessfulRegistrationForDuplicatedBookingDateTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/medical-slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto))
        ).andExpect(status().is4xxClientError());
    }


    MedicalSlotRegistrationDto registrationDtoForNonFoundMedicalAppointment = new MedicalSlotRegistrationDto(
        new MedicalLicenseNumber("12345677", "CA", "US"),
        LocalDateTime.parse("2024-12-12T12:30:30")
    );

    @Test
    @Order(3)
    void testUnsuccessfulRegistrationForNonFoundMedicalAppointment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/medical-slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDtoForNonFoundMedicalAppointment))
        ).andExpect(status().is4xxClientError());
    }
}

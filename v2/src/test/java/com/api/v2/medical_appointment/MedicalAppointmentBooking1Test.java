package com.api.v2.medical_appointment;

import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
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
class MedicalAppointmentBooking1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    MedicalAppointmentBookingDto bookingDto = new MedicalAppointmentBookingDto(
            "",
            LocalDateTime.parse("2025-12-12T12:30:30"),
            ""
    );

    @Order(1)
    @Test
    void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/medical-appointments/public-insurance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
        ).andExpect(status().is2xxSuccessful());
    }

    @Order(2)
    @Test
    void testUnSuccessfulRegistrationForDuplicatedBookingDateTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/medical-appointments/public-insurance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDto))
        ).andExpect(status().is4xxClientError());
    }

    MedicalAppointmentBookingDto bookingDtoWithWrongCustomerId = new MedicalAppointmentBookingDto(
            "",
            LocalDateTime.parse("2025-12-12T12:30:30"),
            ""
    );

    @Order(3)
    @Test
    void testUnSuccessfulRegistrationForWrongCustomerId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/medical-appointments/public-insurance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDtoWithWrongCustomerId))
        ).andExpect(status().is4xxClientError());
    }

    MedicalAppointmentBookingDto bookingDtoWithWrongMedicalSlotId = new MedicalAppointmentBookingDto(
            "",
            LocalDateTime.parse("2025-12-12T12:30:30"),
            ""
    );

    @Order(3)
    @Test
    void testUnSuccessfulRegistrationForWrongMedicalSlotId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/medical-appointments/public-insurance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDtoWithWrongMedicalSlotId))
        ).andExpect(status().is4xxClientError());
    }

}

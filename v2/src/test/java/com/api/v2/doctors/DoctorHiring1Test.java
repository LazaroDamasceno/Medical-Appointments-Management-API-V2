package com.api.v2.doctors;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.common.States;
import com.api.v2.people.dtos.PersonRegistrationDto;
import com.api.v2.people.utils.Gender;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DoctorHiring1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    DoctorHiringDto hiringDto = new DoctorHiringDto(
            new PersonRegistrationDto(
                    "Leo",
                    "",
                    "Santos",
                    LocalDate.parse("2000-12-12"),
                    "123456789",
                    "leosantos@mail.com",
                    "1234567890",
                    Gender.CIS_MALE
            ),
            new MedicalLicenseNumber("87654321", States.CA)
    );

    @Test
    @Order(1)
    void testSuccessfulHiring() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hiringDto))
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void testUnSuccessfulHiringForDuplicatedSsn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hiringDto))
        ).andExpect(status().is4xxClientError());
    }

    DoctorHiringDto hiringDtoWithDuplicatedEmail = new DoctorHiringDto(
            new PersonRegistrationDto(
                    "Leo",
                    "",
                    "Santos",
                    LocalDate.parse("2000-12-12"),
                    "123456788",
                    "leosantos@mail.com",
                    "1234567890",
                    Gender.CIS_MALE
            ),
            new MedicalLicenseNumber("8765321", States.CA)
    );

    @Test
    @Order(3)
    void testUnSuccessfulHiringForDuplicatedEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hiringDtoWithDuplicatedEmail))
        ).andExpect(status().is4xxClientError());
    }

    DoctorHiringDto hiringDtoWithDuplicatedMedicalLicenseNumber = new DoctorHiringDto(
            new PersonRegistrationDto(
                    "Leo",
                    "",
                    "Santos",
                    LocalDate.parse("2000-12-12"),
                    "123456787",
                    "leosanteos@mail.com",
                    "1234567890",
                    Gender.CIS_MALE
            ),
            new MedicalLicenseNumber("87654321", States.CA)
    );

    @Test
    @Order(4)
    void testUnSuccessfulHiringForDuplicatedMedicalLicenseNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hiringDtoWithDuplicatedMedicalLicenseNumber))
        ).andExpect(status().is4xxClientError());
    }

}

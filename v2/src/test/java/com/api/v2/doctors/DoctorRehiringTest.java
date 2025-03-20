package com.api.v2.doctors;

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
class DoctorRehiringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testSuccessfulRehiring() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v2/doctors/12345678/AK/rehiring")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void testUnsuccessfulRehiringForNonFoundMedicalLicenseNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v2/doctors/12345677/AK/rehiring")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    void testUnsuccessfulRehiringForAlreadyActiveDoctor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v2/doctors/12345678/AK/rehiring")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

}

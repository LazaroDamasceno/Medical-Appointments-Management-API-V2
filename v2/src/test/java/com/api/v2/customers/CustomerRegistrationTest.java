package com.api.v2.customers;

import com.api.v2.common.Address;
import com.api.v2.common.States;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.people.dtos.PersonRegistrationDto;
import com.api.v2.people.enums.Gender;
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
class CustomerRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    CustomerRegistrationDto registrationDto = new CustomerRegistrationDto(
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
            new Address(
                    States.CA,
                    "LA",
                    "Downtown",
                    "90012"
            )
    );

    @Test
    @Order(1)
    void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto))
        ).andExpect(status().is2xxSuccessful());
    }

    CustomerRegistrationDto registrationDtoWithDuplicatedSsn = new CustomerRegistrationDto(
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
            new Address(
                    States.CA,
                    "LA",
                    "Downtown",
                    "90012"
            )
    );

    @Test
    @Order(2)
    void testUnSuccessfulRegistrationForDuplicatedSsn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDtoWithDuplicatedSsn))
        ).andExpect(status().is4xxClientError());
    }

    CustomerRegistrationDto registrationDtoWithDuplicatedEmail = new CustomerRegistrationDto(
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
            new Address(
                    States.CA,
                    "LA",
                    "Downtown",
                    "90012"
            )
    );

    @Test
    @Order(3)
    void testUnSuccessfulRegistrationForDuplicatedEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v2/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDtoWithDuplicatedEmail))
        ).andExpect(status().is4xxClientError());
    }

}

package com.api.v2.customers.services;

import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import org.springframework.http.ResponseEntity;

public interface CustomerRegistrationService {
    ResponseEntity<CustomerResponseDto> register(CustomerRegistrationDto registrationDto);
}

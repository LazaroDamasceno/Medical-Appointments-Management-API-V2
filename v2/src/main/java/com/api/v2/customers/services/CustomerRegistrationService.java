package com.api.v2.customers.services;

import com.api.v2.customers.requests.CustomerRegistrationDto;
import com.api.v2.customers.responses.CustomerResponseDto;
import org.springframework.http.ResponseEntity;

public interface CustomerRegistrationService {
    ResponseEntity<CustomerResponseDto> register(CustomerRegistrationDto registrationDto);
}

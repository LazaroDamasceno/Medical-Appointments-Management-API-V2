package com.api.v3.customers.services;

import com.api.v3.customers.dtos.CustomerRegistrationDto;
import com.api.v3.customers.dtos.CustomerResponseDto;

public interface CustomerRegistrationService {
    CustomerResponseDto register(CustomerRegistrationDto registrationDto);
}

package com.api.v2.customers.services;

import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.CustomerResponseDto;

public interface CustomerRegistrationService {
    CustomerResponseDto register(CustomerRegistrationDto registrationDto);
}

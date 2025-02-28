package com.api.v2.customers.services;

import com.api.v2.common.Response;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;

public interface CustomerRegistrationService {
    Response<CustomerResponseDto> register(CustomerRegistrationDto registrationDto);
}

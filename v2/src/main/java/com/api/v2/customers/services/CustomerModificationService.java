package com.api.v2.customers.services;

import com.api.v2.customers.dtos.CustomerModificationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;

public interface CustomerModificationService {
    CustomerResponseDto modify(String id, CustomerModificationDto modificationDto);
}

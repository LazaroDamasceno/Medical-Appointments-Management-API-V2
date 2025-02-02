package com.api.v2.customers.services;

import com.api.v2.customers.dtos.CustomerModificationDto;
import com.api.v2.customers.dtos.CustomerResponseDto;

public interface CustomerModificationService {
    CustomerResponseDto modify(String id, CustomerModificationDto modificationDto);
}

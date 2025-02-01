package com.api.v2.customers.services;

import com.api.v2.customers.dtos.CustomerModificationDto;

public interface CustomerModificationService {
    void modify(String id, CustomerModificationDto modificationDto);
}

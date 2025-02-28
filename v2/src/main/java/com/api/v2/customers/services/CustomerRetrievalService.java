package com.api.v2.customers.services;

import com.api.v2.customers.dtos.exposed.CustomerResponseDto;

import java.util.List;

public interface CustomerRetrievalService {
    CustomerResponseDto findById(String id);
    List<CustomerResponseDto> findAll();
}

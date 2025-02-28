package com.api.v2.customers.services;

import com.api.v2.common.Response;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;

import java.util.List;

public interface CustomerRetrievalService {
    Response<CustomerResponseDto> findById(String id);
    Response<List<CustomerResponseDto>> findAll();
}

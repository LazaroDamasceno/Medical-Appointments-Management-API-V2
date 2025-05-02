package com.api.v2.customers.services;

import com.api.v2.customers.responses.CustomerResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerRetrievalService {
    ResponseEntity<CustomerResponseDto> findById(String id);
    ResponseEntity<List<CustomerResponseDto>> findAll();
}

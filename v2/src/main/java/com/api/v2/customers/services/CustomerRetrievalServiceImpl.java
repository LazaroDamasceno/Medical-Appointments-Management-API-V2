package com.api.v2.customers.services;

import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerFinder;
import com.api.v2.customers.utils.CustomerResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRetrievalServiceImpl implements CustomerRetrievalService {

    private final CustomerRepository customerRepository;
    private final CustomerFinder customerFinder;

    public CustomerRetrievalServiceImpl(CustomerRepository customerRepository,
                                        CustomerFinder customerFinder
    ) {
        this.customerRepository = customerRepository;
        this.customerFinder = customerFinder;
    }

    @Override
    public ResponseEntity<CustomerResponseDto> findById(String id) {
        Customer customer = customerFinder.findById(id);
        CustomerResponseDto responseDto = CustomerResponseMapper.mapToDto(customer);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<List<CustomerResponseDto>> findAll() {
        List<CustomerResponseDto> list = customerRepository
                .findAll()
                .stream()
                .map(CustomerResponseMapper::mapToDto)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}

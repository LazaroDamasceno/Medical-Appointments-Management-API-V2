package com.api.v2.customers.services;

import com.api.v2.common.Id;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.customers.utils.CustomerResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRetrievalServiceImpl implements CustomerRetrievalService {

    private final CustomerRepository customerRepository;
    private final CustomerFinderUtil customerFinderUtil;

    public CustomerRetrievalServiceImpl(CustomerRepository customerRepository,
                                        CustomerFinderUtil customerFinderUtil
    ) {
        this.customerRepository = customerRepository;
        this.customerFinderUtil = customerFinderUtil;
    }

    @Override
    public CustomerResponseDto findById(@Id String id) {
        return CustomerResponseMapper.mapToDto(customerFinderUtil.findById(id));
    }

    @Override
    public List<CustomerResponseDto> findAll() {
        return customerRepository
                .findAll()
                .stream()
                .map(CustomerResponseMapper::mapToDto)
                .toList();
    }
}

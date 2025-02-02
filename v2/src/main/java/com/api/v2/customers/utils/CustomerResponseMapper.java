package com.api.v2.customers.utils;

import com.api.v2.common.AddressResponseMapper;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.people.utils.PersonResponseMapper;

public class CustomerResponseMapper {
    public static CustomerResponseDto mapToResource(Customer customer) {
        return new CustomerResponseDto(
                PersonResponseMapper.mapToResource(customer.getPerson()),
                AddressResponseMapper.mapToString(customer.getAddress())
        );
    }
}

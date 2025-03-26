package com.api.v2.customers.utils;

import com.api.v2.common.AddressResponseMapper;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.people.utils.PersonResponseMapper;

public final class CustomerResponseMapper {
    public static CustomerResponseDto toDto(Customer customer) {
        return new CustomerResponseDto(
                PersonResponseMapper.toResource(customer.getPerson()),
                AddressResponseMapper.mapToString(customer.getAddress())
        );
    }
}

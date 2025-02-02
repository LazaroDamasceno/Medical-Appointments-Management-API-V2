package com.api.v2.customers.utils;

import com.api.v2.common.AddressResponseMapper;
import com.api.v2.customers.domain.Customer;
import com.api.v2.customers.dtos.CustomerResponseDto;
import com.api.v2.people.utils.PersonResponseMapper;

public class CustomerResponseMapper {
    public static CustomerResponseDto mapToDto(Customer customer) {
        return new CustomerResponseDto(
                PersonResponseMapper.mapToDto(customer.getPerson()),
                AddressResponseMapper.mapToString(customer.getAddress())
        );
    }
}

package com.api.v2.customers.utils;

import com.api.v2.customers.domain.Customer;
import com.api.v2.customers.dtos.CustomerResponseDto;
import com.api.v2.people.utils.PersonResponseMapper;

public class CustomerResponseMapper {
    public static CustomerResponseDto map(Customer customer) {
        return new CustomerResponseDto(
                PersonResponseMapper.map(customer.getPerson()),
                customer.getAddress()
        );
    }
}

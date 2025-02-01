package com.api.v3.customers.utils;

import com.api.v3.customers.domain.Customer;
import com.api.v3.customers.dtos.CustomerResponseDto;
import com.api.v3.people.utils.PersonResponseMapper;

public class CustomerResponseMapper {
    public static CustomerResponseDto map(Customer customer) {
        return new CustomerResponseDto(
                PersonResponseMapper.map(customer.getPerson()),
                customer.getAddress()
        );
    }
}

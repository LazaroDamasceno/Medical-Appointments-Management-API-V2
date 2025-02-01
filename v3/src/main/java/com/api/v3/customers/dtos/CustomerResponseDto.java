package com.api.v3.customers.dtos;

import com.api.v3.common.Address;
import com.api.v3.people.dtos.PersonResponseDto;

public record CustomerResponseDto(
        PersonResponseDto personResponseDto,
        Address address
) {
}

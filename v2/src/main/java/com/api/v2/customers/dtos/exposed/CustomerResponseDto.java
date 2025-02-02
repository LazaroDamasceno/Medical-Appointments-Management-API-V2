package com.api.v2.customers.dtos.exposed;

import com.api.v2.people.dtos.PersonResponseDto;

public record CustomerResponseDto(
        PersonResponseDto person,
        String address
) {
}

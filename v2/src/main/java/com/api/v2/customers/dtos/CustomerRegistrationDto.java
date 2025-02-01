package com.api.v2.customers.dtos;

import com.api.v2.common.Address;
import com.api.v2.people.dtos.PersonRegistrationDto;
import jakarta.validation.Valid;

public record CustomerRegistrationDto(
        @Valid PersonRegistrationDto personRegistrationDto,
        @Valid Address address
) {
}

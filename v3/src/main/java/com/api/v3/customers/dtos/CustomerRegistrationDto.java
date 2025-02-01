package com.api.v3.customers.dtos;

import com.api.v3.common.Address;
import com.api.v3.people.dtos.PersonRegistrationDto;
import jakarta.validation.Valid;

public record CustomerRegistrationDto(
        @Valid PersonRegistrationDto personRegistrationDto,
        @Valid Address address
) {
}

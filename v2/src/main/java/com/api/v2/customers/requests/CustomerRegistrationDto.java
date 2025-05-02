package com.api.v2.customers.requests;

import com.api.v2.common.Address;
import com.api.v2.people.requests.PersonRegistrationDto;
import jakarta.validation.Valid;

public record CustomerRegistrationDto(
        @Valid PersonRegistrationDto personRegistrationDto,
        @Valid Address address
) {
}

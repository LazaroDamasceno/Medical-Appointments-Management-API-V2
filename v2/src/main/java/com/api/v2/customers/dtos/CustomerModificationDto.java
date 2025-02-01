package com.api.v2.customers.dtos;

import com.api.v2.common.Address;
import com.api.v2.people.dtos.PersonModificationDto;
import jakarta.validation.Valid;

public record CustomerModificationDto(
        @Valid PersonModificationDto personModificationDto,
        @Valid Address address
) {
}

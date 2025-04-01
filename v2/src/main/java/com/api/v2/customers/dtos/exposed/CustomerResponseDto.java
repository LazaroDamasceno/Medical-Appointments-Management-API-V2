package com.api.v2.customers.dtos.exposed;

import com.api.v2.common.Address;

public record CustomerResponseDto(
        String fullName,
        Address address
) {
}

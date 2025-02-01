package com.api.v3.common;

public record Address(
        String state,
        String city,
        String street,
        String zipcode
) {
}

package com.api.v2.common;

public record Address(
        String state,
        String city,
        String street,
        String zipcode
) {
}

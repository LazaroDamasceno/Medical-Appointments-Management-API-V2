package com.api.v2.common;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank String state,
        @NotBlank String city,
        @NotBlank String street,
        @NotBlank String zipcode
) {
}

package com.api.v2.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Address(
        @NotNull States state,
        @NotBlank String city,
        @NotBlank String street,
        @NotBlank String zipcode
) {
}

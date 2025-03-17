package com.api.v2.doctors.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicalLicenseNumber(
        @NotBlank String licenseNumber,
        @NotBlank String regionCode,
        @NotBlank String countryCode
) {
}

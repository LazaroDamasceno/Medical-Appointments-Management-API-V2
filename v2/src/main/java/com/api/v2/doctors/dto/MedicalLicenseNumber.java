package com.api.v2.doctors.dto;

import com.api.v2.doctors.enums.Regions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicalLicenseNumber(
        @NotBlank String licenseNumber,
        @NotNull Regions region
) {
}

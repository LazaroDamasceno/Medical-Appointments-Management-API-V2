package com.api.v2.doctors.dtos;

import com.api.v2.common.States;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicalLicenseNumber(
        @NotBlank @Size(min = 8, max = 8) String licenseNumber,
        @NotNull States state
) {
}

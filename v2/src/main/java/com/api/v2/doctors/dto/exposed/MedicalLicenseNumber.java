package com.api.v2.doctors.dto.exposed;

import com.api.v2.doctors.enums.Regions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicalLicenseNumber(
        @NotBlank @Size(min = 8, max = 9) String licenseNumber,
        @NotNull Regions region
) {
}

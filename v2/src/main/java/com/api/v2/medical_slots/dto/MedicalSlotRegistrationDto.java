package com.api.v2.medical_slots.dto;


import com.api.v2.doctors.dto.MedicalLicenseNumber;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalSlotRegistrationDto(
        MedicalLicenseNumber medicalLicenseNumber,
        @NotNull LocalDateTime availableAt
) {
}

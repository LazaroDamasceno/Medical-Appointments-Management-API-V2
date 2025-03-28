package com.api.v2.medical_slots.dto;


import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalSlotRegistrationDto(
        @Valid MedicalLicenseNumber medicalLicenseNumber,
        @NotNull LocalDateTime availableAt
) {
}

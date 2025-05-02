package com.api.v2.medical_slots.requests;


import com.api.v2.doctors.dtos.MedicalLicenseNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalSlotRegistrationDto(
        @Valid MedicalLicenseNumber medicalLicenseNumber,
        @NotNull LocalDateTime availableAt
) {
}

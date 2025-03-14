package com.api.v2.medical_slots.dto;


import com.api.v2.common.MLN;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalSlotRegistrationDto(
        @MLN String medicalLicenseNumber,
        @NotNull LocalDateTime availableAt
) {
}

package com.api.v2.medical_slots.dto;


import java.time.LocalDateTime;

public record MedicalSlotRegistrationDto(
        String medicalLicenseNumber,
        LocalDateTime availableAt
) {
}

package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

import java.time.LocalDateTime;

public interface MedicalSlotRegistrationService {
    MedicalSlotResponseResource register(
            String medicalLicenseNumber,
            LocalDateTime availableAt
    );
}

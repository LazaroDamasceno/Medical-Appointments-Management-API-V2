package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.dtos.MedicalSlotResponseDto;

public interface MedicalSlotCancellationService {
    MedicalSlotResponseDto cancelById(String medicalLicenseNumber, String slotId);
}

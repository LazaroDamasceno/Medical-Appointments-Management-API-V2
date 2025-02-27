package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.dtos.MedicalSlotResponseDto;

public interface MedicalSlotCompletionService {
    MedicalSlotResponseDto completeById(String medicalLicenseNumber, String slotId);
}

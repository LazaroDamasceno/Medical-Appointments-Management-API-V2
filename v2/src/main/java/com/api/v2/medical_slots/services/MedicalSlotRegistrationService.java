package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.dtos.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.dtos.MedicalSlotResponseDto;

public interface MedicalSlotRegistrationService {
    MedicalSlotResponseDto register(MedicalSlotRegistrationDto registrationDto);
}

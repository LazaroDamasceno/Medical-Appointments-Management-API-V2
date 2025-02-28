package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotRegistrationService {
    ResponseEntity<MedicalSlotResponseResource> register(MedicalSlotRegistrationDto registrationDto);
}

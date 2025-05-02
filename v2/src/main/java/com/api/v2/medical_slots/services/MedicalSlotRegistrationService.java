package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.requests.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.responses.MedicalSlotResponseResource;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotRegistrationService {
    ResponseEntity<MedicalSlotResponseResource> register(MedicalSlotRegistrationDto registrationDto);
}

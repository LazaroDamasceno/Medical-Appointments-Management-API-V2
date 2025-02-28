package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotCompletionService {
    ResponseEntity<MedicalSlotResponseResource> completeById(String medicalLicenseNumber, String slotId);
}

package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotCompletionService {
    ResponseEntity<ResourceResponse> completeById(String medicalLicenseNumber, String slotId);
}

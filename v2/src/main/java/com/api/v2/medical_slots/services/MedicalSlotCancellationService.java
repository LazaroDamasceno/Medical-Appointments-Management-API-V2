package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotCancellationService {
    ResponseEntity<ResourceResponse> cancelById(String medicalLicenseNumber, String slotId);
}

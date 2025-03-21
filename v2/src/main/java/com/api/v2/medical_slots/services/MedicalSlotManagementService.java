package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotManagementService {
    ResponseEntity<ResourceResponse> cancelById(String medicalLicenseNumber, String state, String slotId);
    ResponseEntity<ResourceResponse> completeById(String medicalLicenseNumber, String state, String slotId);
}

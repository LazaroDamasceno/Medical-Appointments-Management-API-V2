package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.dto.MedicalLicenseNumber;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotCompletionService {
    ResponseEntity<ResourceResponse> completeById(MedicalLicenseNumber medicalLicenseNumber, String slotId);
}

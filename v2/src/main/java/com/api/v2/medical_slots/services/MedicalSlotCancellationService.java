package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import org.springframework.http.ResponseEntity;

public interface MedicalSlotCancellationService {
    ResponseEntity<ResourceResponse> cancelById(MedicalLicenseNumber medicalLicenseNumber, String slotId);
}

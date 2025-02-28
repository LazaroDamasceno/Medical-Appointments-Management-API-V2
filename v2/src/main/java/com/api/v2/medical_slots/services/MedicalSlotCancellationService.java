package com.api.v2.medical_slots.services;

import com.api.v2.common.Response;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public interface MedicalSlotCancellationService {
    Response<MedicalSlotResponseResource> cancelById(String medicalLicenseNumber, String slotId);
}

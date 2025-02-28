package com.api.v2.medical_slots.services;

import com.api.v2.common.Response;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public interface MedicalSlotCompletionService {
    Response<MedicalSlotResponseResource> completeById(String medicalLicenseNumber, String slotId);
}

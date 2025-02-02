package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public interface MedicalSlotCancellationService {
    MedicalSlotResponseResource cancel(String id);
}

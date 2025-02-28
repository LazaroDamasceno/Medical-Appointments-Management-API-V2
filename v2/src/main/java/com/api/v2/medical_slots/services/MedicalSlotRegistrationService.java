package com.api.v2.medical_slots.services;

import com.api.v2.common.Response;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public interface MedicalSlotRegistrationService {
    Response<MedicalSlotResponseResource> register(MedicalSlotRegistrationDto registrationDto);
}

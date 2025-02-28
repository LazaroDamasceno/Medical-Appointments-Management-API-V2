package com.api.v2.medical_slots.services;

import com.api.v2.common.Response;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

import java.util.List;

public interface MedicalSlotRetrievalService {
    Response<MedicalSlotResponseResource> findById(String medicalLicenseNumber, String slotId);
    Response<List<MedicalSlotResponseResource>> findAllByDoctor(String medicalLicenseNumber);
    Response<List<MedicalSlotResponseResource>> findAll();
}

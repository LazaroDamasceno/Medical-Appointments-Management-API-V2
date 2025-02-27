package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

import java.util.List;

public interface MedicalSlotRetrievalService {
    MedicalSlotResponseResource findById(String medicalLicenseNumber, String slotId);
    List<MedicalSlotResponseResource> findAllByDoctor(String medicalLicenseNumber);
    List<MedicalSlotResponseResource> findAll();
}

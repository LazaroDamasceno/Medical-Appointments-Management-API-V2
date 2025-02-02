package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

import java.util.List;

public interface MedicalSlotRetrievalService {
    MedicalSlotResponseResource findById(String id);
    List<MedicalSlotResponseResource> findByDoctor(String medicalLicenseNumber);
    List<MedicalSlotResponseResource> findAll();
}

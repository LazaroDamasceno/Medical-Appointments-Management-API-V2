package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.responses.MedicalSlotResponseResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalSlotRetrievalService {
    ResponseEntity<MedicalSlotResponseResource> findById(String medicalLicenseNumber, String state, String slotId);
    ResponseEntity<List<MedicalSlotResponseResource> >findAllByDoctor(String medicalLicenseNumber, String state);
    ResponseEntity<List<MedicalSlotResponseResource>> findAll();
}

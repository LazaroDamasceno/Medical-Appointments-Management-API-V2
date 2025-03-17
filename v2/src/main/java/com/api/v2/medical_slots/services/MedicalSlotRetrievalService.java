package com.api.v2.medical_slots.services;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalSlotRetrievalService {
    ResponseEntity<MedicalSlotResponseResource> findById(MedicalLicenseNumber medicalLicenseNumber, String slotId);
    ResponseEntity<List<MedicalSlotResponseResource> >findAllByDoctor(MedicalLicenseNumber medicalLicenseNumber);
    ResponseEntity<List<MedicalSlotResponseResource>> findAll();
}

package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.dtos.MedicalSlotResponseDto;

import java.util.List;

public interface MedicalSlotRetrievalService {
    MedicalSlotResponseDto findById(String medicalLicenseNumber, String slotId);
    List<MedicalSlotResponseDto> findAllByDoctor(String medicalLicenseNumber);
    List<MedicalSlotResponseDto> findAll();
}

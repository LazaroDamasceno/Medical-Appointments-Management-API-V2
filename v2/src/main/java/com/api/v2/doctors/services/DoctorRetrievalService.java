package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.DoctorResponseDto;

import java.util.List;

public interface DoctorRetrievalService {
    DoctorResponseDto findByMedicalLicenseNumber(String medicalLicenseNumber);
    List<DoctorResponseDto> findAll();
}

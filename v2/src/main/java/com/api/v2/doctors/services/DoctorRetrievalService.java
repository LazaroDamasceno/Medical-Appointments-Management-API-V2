package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;

import java.util.List;

public interface DoctorRetrievalService {
    DoctorResponseResource findByMedicalLicenseNumber(String medicalLicenseNumber);
    List<DoctorResponseResource> findAll();
}

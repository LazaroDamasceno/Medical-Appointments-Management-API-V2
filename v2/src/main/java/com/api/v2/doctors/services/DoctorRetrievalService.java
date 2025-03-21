package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorRetrievalService {
    ResponseEntity<DoctorResponseResource> findByMedicalLicenseNumber(String medicalLicenseNumber, String state);
    ResponseEntity<List<DoctorResponseResource>> findAll();
}

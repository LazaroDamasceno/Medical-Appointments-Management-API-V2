package com.api.v2.doctors.services;

import com.api.v2.common.Response;
import com.api.v2.doctors.resources.DoctorResponseResource;

import java.util.List;

public interface DoctorRetrievalService {
    Response<DoctorResponseResource> findByMedicalLicenseNumber(String medicalLicenseNumber);
    Response<List<DoctorResponseResource>> findAll();
}

package com.api.v2.doctors.responses;

import org.springframework.hateoas.RepresentationModel;

import com.api.v2.doctors.dtos.MedicalLicenseNumber;

public class DoctorResponseResource extends RepresentationModel<DoctorResponseResource> {

    private final String fullName;
    private final MedicalLicenseNumber medicalLicenseNumber;

    public DoctorResponseResource(String fullName, MedicalLicenseNumber medicalLicenseNumber) {
        this.fullName = fullName;
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public MedicalLicenseNumber getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }
}

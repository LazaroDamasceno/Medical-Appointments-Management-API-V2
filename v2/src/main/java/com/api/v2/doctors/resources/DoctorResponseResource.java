package com.api.v2.doctors.resources;

import org.springframework.hateoas.RepresentationModel;

import com.api.v2.doctors.dto.MedicalLicenseNumber;

public class DoctorResponseResource extends RepresentationModel<DoctorResponseResource> {

    private String fullName;
    private MedicalLicenseNumber medicalLicenseNumber;

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

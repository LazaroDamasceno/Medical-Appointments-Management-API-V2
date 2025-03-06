package com.api.v2.doctors.resources;

import org.springframework.hateoas.RepresentationModel;

public class DoctorResponseResource extends RepresentationModel<DoctorResponseResource> {

    private String fullName;
    private String medicalLicenseNumber;

    public DoctorResponseResource(String fullName, String medicalLicenseNumber) {
        this.fullName = fullName;
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }
}

package com.api.v2.doctors.resources;

import com.api.v2.people.dtos.PersonResponseDto;

public class DoctorResponseResource {

    private final PersonResponseDto person;
    private final String medicalLicenseNumber;

    public DoctorResponseResource(PersonResponseDto person, String medicalLicenseNumber) {
        this.person = person;
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public PersonResponseDto getPerson() {
        return person;
    }

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }
}

package com.api.v2.doctors.dto.exposed;

import com.api.v2.common.ErrorResponse;
import com.api.v2.people.dtos.PersonResponseDto;

public class DoctorResponseDto extends ErrorResponse {

    private final PersonResponseDto person;
    private final String medicalLicenseNumber;

    public DoctorResponseDto(PersonResponseDto person, String medicalLicenseNumber) {
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

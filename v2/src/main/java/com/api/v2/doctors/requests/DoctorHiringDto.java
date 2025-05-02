package com.api.v2.doctors.requests;

import com.api.v2.doctors.dtos.MedicalLicenseNumber;
import com.api.v2.people.requests.PersonRegistrationDto;
import jakarta.validation.Valid;

public record DoctorHiringDto(
        @Valid PersonRegistrationDto personRegistrationDto,
        @Valid MedicalLicenseNumber medicalLicenseNumber
) {
}

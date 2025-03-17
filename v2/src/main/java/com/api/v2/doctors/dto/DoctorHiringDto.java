package com.api.v2.doctors.dto;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.people.dtos.PersonRegistrationDto;
import jakarta.validation.Valid;

public record DoctorHiringDto(
        @Valid PersonRegistrationDto personRegistrationDto,
        @Valid MedicalLicenseNumber medicalLicenseNumber
) {
}

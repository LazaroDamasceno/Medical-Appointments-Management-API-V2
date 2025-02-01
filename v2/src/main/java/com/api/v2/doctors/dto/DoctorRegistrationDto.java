package com.api.v2.doctors.dto;

import com.api.v2.people.dtos.PersonRegistrationDto;
import jakarta.validation.Valid;

public record DoctorRegistrationDto(
        @Valid PersonRegistrationDto personRegistrationDto,
        String medicalLicenseNumber
) {
}

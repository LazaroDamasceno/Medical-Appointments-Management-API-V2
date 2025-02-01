package com.api.v2.doctors.dto;

import com.api.v2.people.dtos.PersonResponseDto;

public record DoctorResponseDto(
        PersonResponseDto person,
        String medicalLicenseNumber
) {
}

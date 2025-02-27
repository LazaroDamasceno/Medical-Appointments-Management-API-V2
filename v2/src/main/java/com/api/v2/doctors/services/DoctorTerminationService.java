package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.exposed.DoctorResponseDto;

public interface DoctorTerminationService {
    DoctorResponseDto terminate(String medicalLicenseNumber);
}

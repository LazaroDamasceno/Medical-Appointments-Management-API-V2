package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.exposed.DoctorResponseDto;

public interface DoctorRehireService {
    DoctorResponseDto rehire(String medicalLicenseNumber);
}

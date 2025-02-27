package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.dto.exposed.DoctorResponseDto;

public interface DoctorHiringService {
    DoctorResponseDto hire(DoctorHiringDto hiringDto);
}

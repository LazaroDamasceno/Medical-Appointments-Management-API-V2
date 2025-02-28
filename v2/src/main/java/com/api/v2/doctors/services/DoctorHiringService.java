package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.resources.DoctorResponseResource;

public interface DoctorHiringService {
    DoctorResponseResource hire(DoctorHiringDto hiringDto);
}

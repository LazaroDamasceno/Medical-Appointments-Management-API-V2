package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.http.ResponseEntity;

public interface DoctorHiringService {
    ResponseEntity<DoctorResponseResource> hire(DoctorHiringDto hiringDto);
}

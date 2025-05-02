package com.api.v2.doctors.services;

import com.api.v2.doctors.requests.DoctorHiringDto;
import com.api.v2.doctors.responses.DoctorResponseResource;
import org.springframework.http.ResponseEntity;

public interface DoctorHiringService {
    ResponseEntity<DoctorResponseResource> hire(DoctorHiringDto hiringDto);
}

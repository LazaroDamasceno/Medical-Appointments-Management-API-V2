package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.http.ResponseEntity;

public interface DoctorTerminationService {
    ResponseEntity<DoctorResponseResource> terminate(String medicalLicenseNumber);
}

package com.api.v2.doctors.services;

import org.springframework.http.ResponseEntity;

public interface DoctorTerminationService {
    ResponseEntity<Void> terminate(String medicalLicenseNumber);
}

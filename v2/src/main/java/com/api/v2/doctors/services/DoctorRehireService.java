package com.api.v2.doctors.services;

import com.api.v2.common.ResourceResponse;
import org.springframework.http.ResponseEntity;

public interface DoctorRehireService {
    ResponseEntity<ResourceResponse> rehire(String medicalLicenseNumber, String state);
}

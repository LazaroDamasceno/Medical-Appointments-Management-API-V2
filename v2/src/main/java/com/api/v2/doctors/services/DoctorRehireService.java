package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.http.ResponseEntity;

public interface DoctorRehireService {
    ResponseEntity<DoctorResponseResource> rehire(MedicalLicenseNumber medicalLicenseNumber);
}

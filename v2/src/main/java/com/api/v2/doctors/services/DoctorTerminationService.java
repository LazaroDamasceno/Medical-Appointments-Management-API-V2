package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;

public interface DoctorTerminationService {
    DoctorResponseResource terminate(String medicalLicenseNumber);
}

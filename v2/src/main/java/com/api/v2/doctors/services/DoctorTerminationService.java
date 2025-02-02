package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.hateoas.EntityModel;

public interface DoctorTerminationService {
    EntityModel<DoctorResponseResource> terminate(String medicalLicenseNumber);
}

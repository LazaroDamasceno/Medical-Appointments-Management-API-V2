package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.people.dtos.PersonModificationDto;
import org.springframework.hateoas.EntityModel;

public interface DoctorModificationService {
    EntityModel<DoctorResponseResource> modify(String medicalLicenseNumber, PersonModificationDto modificationDto);
}

package com.api.v2.doctors.services;

import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.people.dtos.PersonModificationDto;

public interface DoctorModificationService {
    DoctorResponseResource modify(String medicalLicenseNumber, PersonModificationDto modificationDto);
}

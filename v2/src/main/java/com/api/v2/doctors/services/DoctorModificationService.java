package com.api.v2.doctors.services;

import com.api.v2.people.dtos.PersonModificationDto;

public interface DoctorModificationService {
    void modify(String medicalLicenseNumber, PersonModificationDto modificationDto);
}

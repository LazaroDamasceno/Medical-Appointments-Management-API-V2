package com.api.v2.doctors.services;

import com.api.v2.people.dtos.PersonModificationDto;
import org.springframework.http.ResponseEntity;

public interface DoctorModificationService {
    ResponseEntity<Void> modify(String medicalLicenseNumber, PersonModificationDto modificationDto);
}

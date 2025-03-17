package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.MedicalLicenseNumber;
import com.api.v2.doctors.exceptions.NonExistentDoctorException;
import org.springframework.stereotype.Component;

@Component
public class DoctorFinder {

    private final DoctorRepository doctorRepository;

    public DoctorFinder(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor findByMedicalLicenseNumber(MedicalLicenseNumber medicalLicenseNumber) {
        return doctorRepository
                .findAll()
                .stream()
                .filter(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber))
                .findFirst()
                .orElseThrow(() -> new NonExistentDoctorException(medicalLicenseNumber.licenseNumber()));

    }
}

package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.exceptions.NonExistentDoctorException;
import org.springframework.stereotype.Component;

@Component
public class DoctorFinderUtil {

    private DoctorRepository doctorRepository;

    public DoctorFinderUtil(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor findByMedicalLicenseNumber(String medicalLicenseNumber) {
        return doctorRepository
                .findAll()
                .stream()
                .filter(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber))
                .findFirst()
                .orElseThrow(() -> new NonExistentDoctorException(medicalLicenseNumber));

    }
}

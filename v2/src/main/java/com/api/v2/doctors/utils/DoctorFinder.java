package com.api.v2.doctors.utils;

import com.api.v2.common.States;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.exceptions.NonExistentDoctorException;
import org.springframework.stereotype.Component;

@Component
public class DoctorFinder {

    private final DoctorRepository doctorRepository;

    public DoctorFinder(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor findByMedicalLicenseNumber(String licenseNumber, String state) {
        MedicalLicenseNumber medicalLicenseNumber = new MedicalLicenseNumber(licenseNumber, States.from(state));
        return doctorRepository
                .findAll()
                .stream()
                .filter(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber))
                .findFirst()
                .orElseThrow(() -> new NonExistentDoctorException(licenseNumber, state));

    }

    public Doctor findByMedicalLicenseNumber(MedicalLicenseNumber medicalLicenseNumber) {
        return doctorRepository
                .findAll()
                .stream()
                .filter(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber))
                .findFirst()
                .orElseThrow(() -> new NonExistentDoctorException(
                        medicalLicenseNumber.licenseNumber(),
                        medicalLicenseNumber.state().toString()
                ));

    }
}

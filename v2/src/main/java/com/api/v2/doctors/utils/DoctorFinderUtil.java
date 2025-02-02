package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.exceptions.NonExistentDoctorException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DoctorFinderUtil {

    private final DoctorRepository doctorRepository;

    public DoctorFinderUtil(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor findByMedicalLicenseNumber(String medicalLicenseNumber) {
        Optional<Doctor> optionalDoctor = doctorRepository
                .findAll()
                .stream()
                .filter(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber))
                .findFirst();
        if (optionalDoctor.isEmpty()) {
            throw new NonExistentDoctorException(medicalLicenseNumber);
        }
        return optionalDoctor.get();
    }
}

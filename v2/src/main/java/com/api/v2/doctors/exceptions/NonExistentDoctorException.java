package com.api.v2.doctors.exceptions;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;

public class NonExistentDoctorException extends RuntimeException {
    public NonExistentDoctorException(MedicalLicenseNumber medicalLicenseNumber) {
        super("Doctor whose medical license number is %s/%s was not found.".formatted(medicalLicenseNumber.licenseNumber(),
                                                                                        medicalLicenseNumber.state().toString()
        ));
    }
}

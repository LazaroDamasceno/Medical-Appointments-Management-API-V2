package com.api.v2.doctors.exceptions;

public class NonExistentDoctorException extends RuntimeException {
    public NonExistentDoctorException(String medicalLicenseNumber) {
        super("Doctor whose medical license number is %s was not found.".formatted(medicalLicenseNumber));
    }
}

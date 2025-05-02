package com.api.v2.doctors.exceptions;

public class NonExistentDoctorException extends RuntimeException {
    public NonExistentDoctorException(String licenseNumber, String state) {
        super("Doctor whose medical license number is %s/%s was not found.".formatted(licenseNumber, state));
    }
}

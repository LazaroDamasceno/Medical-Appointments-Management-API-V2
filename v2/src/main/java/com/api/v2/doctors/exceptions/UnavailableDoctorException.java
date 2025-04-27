package com.api.v2.doctors.exceptions;

public class UnavailableDoctorException extends RuntimeException {
    public UnavailableDoctorException() {
        super("Sought doctor is terminated.");
    }
}

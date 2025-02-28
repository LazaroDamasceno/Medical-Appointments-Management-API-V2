package com.api.v2.doctors.exceptions;

public class ImmutableDoctorStatusException extends RuntimeException {
    public ImmutableDoctorStatusException(String message) {
        super(message);
    }
}

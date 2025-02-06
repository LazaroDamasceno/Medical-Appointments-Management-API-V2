package com.api.v2.medical_appointments.exceptions;

public class ImmutableMedicalAppointmentException extends RuntimeException {
    public ImmutableMedicalAppointmentException(String message) {
        super(message);
    }
}

package com.api.v2.medical_appointments.exceptions;

public class ImmutableMedicalAppointmentStatusException extends RuntimeException {
    public ImmutableMedicalAppointmentStatusException(String message) {
        super(message);
    }
}

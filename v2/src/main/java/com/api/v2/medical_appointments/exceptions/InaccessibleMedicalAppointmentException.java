package com.api.v2.medical_appointments.exceptions;

public class InaccessibleMedicalAppointmentException extends RuntimeException {
    public InaccessibleMedicalAppointmentException(String message) {
        super(message);
    }
}

package com.api.v2.medical_appointments.exceptions;

public class InaccessibleMedicalAppointmentException extends RuntimeException {
    public InaccessibleMedicalAppointmentException(String customerId, String medicalAppointmentId) {
        super("Customer whose id is %s is not associated with the medical appointment whose id is  %s"
                .formatted(customerId, medicalAppointmentId));
    }
}

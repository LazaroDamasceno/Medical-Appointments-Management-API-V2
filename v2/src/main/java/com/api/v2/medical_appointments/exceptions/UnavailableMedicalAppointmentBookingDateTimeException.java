package com.api.v2.medical_appointments.exceptions;

public class UnavailableMedicalAppointmentBookingDateTimeException extends RuntimeException {
    public UnavailableMedicalAppointmentBookingDateTimeException() {
        super("The given booking datetime is already associated with an active medical appointment.");
    }
}

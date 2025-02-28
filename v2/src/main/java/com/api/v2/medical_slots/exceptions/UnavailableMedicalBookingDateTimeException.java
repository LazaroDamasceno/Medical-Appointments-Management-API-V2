package com.api.v2.medical_slots.exceptions;

public class UnavailableMedicalBookingDateTimeException extends RuntimeException {
    public UnavailableMedicalBookingDateTimeException() {
        super("The given booking datetime is already associated with an active medical slot.");
    }
}

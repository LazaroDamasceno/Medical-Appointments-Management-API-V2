package com.api.v2.common;

public class UnavailableBookingDateTimeException extends RuntimeException {
    public UnavailableBookingDateTimeException() {
        super("The given booking datetime is already associated with an active medical slot.");
    }
}

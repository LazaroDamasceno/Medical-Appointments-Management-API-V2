package com.api.v2.common;

import java.time.LocalDate;

public final class PastDateHandler {

    public static void handle(LocalDate date) {
        if (PastDateChecker.isBeforeToday(date)) {
            throw new PastBookingDateException();
        }
    }

}

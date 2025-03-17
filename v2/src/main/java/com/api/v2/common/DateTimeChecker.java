package com.api.v2.common;

import java.time.LocalDate;

public final class DateTimeChecker {

    public static boolean isBeforeToday(LocalDate date) {
        return LocalDate.now().isBefore(date);
    }
}

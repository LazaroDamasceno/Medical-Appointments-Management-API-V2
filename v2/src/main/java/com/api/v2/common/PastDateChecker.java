package com.api.v2.common;

import java.time.LocalDate;

public final class PastDateChecker {

    public static boolean isBeforeToday(LocalDate date) {
        return LocalDate.now().isAfter(date);
    }
}

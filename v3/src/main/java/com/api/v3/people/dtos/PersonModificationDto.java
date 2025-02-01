package com.api.v3.people.dtos;

import java.time.LocalDate;

public record PersonModificationDto(
        String firstName,
        String middleName,
        String lastName,
        LocalDate birthDate,
        String email,
        String phoneNumber,
        String gender
) {
}

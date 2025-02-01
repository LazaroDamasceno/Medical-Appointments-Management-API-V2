package com.api.v2.people.dtos;

import java.time.LocalDate;

public record PersonRegistrationDto(
        String firstName,
        String middleName,
        String lastName,
        LocalDate birthDate,
        String ssn,
        String email,
        String phoneNumber,
        String gender
) {
}

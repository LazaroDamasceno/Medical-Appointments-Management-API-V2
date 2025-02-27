package com.api.v2.people.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PersonModificationDto(
        @NotBlank
        String firstName,
        String middleName,
        @NotBlank
        String lastName,
        @NotNull
        LocalDate birthDate,
        @Email(message = "Type a proper type of email")
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 10, max = 10, message = "Phone number has 10 digits.")
        String phoneNumber,
        @NotBlank
        @Size(min = 1)
        String gender
) {
}

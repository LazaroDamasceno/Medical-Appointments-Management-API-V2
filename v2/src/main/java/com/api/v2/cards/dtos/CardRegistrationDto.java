package com.api.v2.cards.dtos;

import java.time.LocalDate;

public record CardRegistrationDto(
        String bank,
        String cvc_cvv,
        LocalDate dueDate
) {
}

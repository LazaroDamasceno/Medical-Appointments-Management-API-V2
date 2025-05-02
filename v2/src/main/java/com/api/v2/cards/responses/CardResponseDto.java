package com.api.v2.cards.responses;

import java.time.LocalDate;

public record CardResponseDto(
        String id,
        String type,
        String bank,
        String cvc_cvv,
        LocalDate dueDate
) {
}

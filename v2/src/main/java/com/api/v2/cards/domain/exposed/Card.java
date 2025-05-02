package com.api.v2.cards.domain.exposed;

import com.api.v2.cards.requests.CardRegistrationDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;
import java.util.UUID;

@Document
public record Card(
        @Id
        String id,
        String type,
        String bank,
        String cvc_cvv,
        LocalDate dueDate,
        LocalDateTime createdAt,
        ZoneId createdAtZoneId,
        ZoneOffset createdAtZoneOffset
) {

    public static Card of(String type, CardRegistrationDto registrationDto) {
        return new Card(
                UUID.randomUUID().toString(),
                type,
                registrationDto.bank(),
                registrationDto.cvc_cvv(),
                registrationDto.dueDate(),
                LocalDateTime.now(),
                ZoneId.systemDefault(),
                OffsetTime.now().getOffset()
        );
    }
}

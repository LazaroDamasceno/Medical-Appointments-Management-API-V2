package com.api.v2.cards.domain.exposed;

import com.api.v2.cards.dtos.CardRegistrationDto;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;

@Document
public record Card(
        @BsonId
        ObjectId id,
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
                new ObjectId(),
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

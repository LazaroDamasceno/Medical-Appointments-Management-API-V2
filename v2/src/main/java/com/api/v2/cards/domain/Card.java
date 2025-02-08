package com.api.v2.cards.domain;

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

    public static Card create(String type, String bank, String cvc_cvv, LocalDate dueDate) {
        return new Card(
                new ObjectId(),
                type,
                bank, cvc_cvv,
                dueDate,
                LocalDateTime.now(),
                ZoneId.systemDefault(),
                OffsetTime.now().getOffset()
        );
    }
}

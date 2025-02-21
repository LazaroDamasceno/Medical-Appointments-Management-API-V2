package com.api.v2.people.domain;

import com.api.v2.common.DstCheckerUtil;
import com.api.v2.people.domain.exposed.Person;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;

@Document
public record PersonAuditTrail(
        @BsonId
        ObjectId id,
        Person person,
        LocalDate createdAt,
        ZoneId createdAtZoneId,
        ZoneOffset createdAtZoneOffset,
        boolean isCreatedDuringDST
) {

    public static PersonAuditTrail of(Person person) {
        return new PersonAuditTrail(
                new ObjectId(),
                person,
                LocalDate.now(),
                ZoneId.systemDefault(),
                OffsetDateTime.now().getOffset(),
                DstCheckerUtil.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault())
        );
    }
}

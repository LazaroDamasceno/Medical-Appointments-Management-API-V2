package com.api.v2.people.domain;

import com.api.v2.common.DstChecker;
import com.api.v2.people.domain.exposed.Person;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;
import java.util.UUID;

@Document
public record PersonAuditTrail(
        @Id
        String id,
        Person person,
        LocalDate createdAt,
        ZoneId createdAtZoneId,
        ZoneOffset createdAtZoneOffset,
        Boolean isCreatedDuringDST
) {

    public static PersonAuditTrail of(Person person) {
        return new PersonAuditTrail(
                UUID.randomUUID().toString(),
                person,
                LocalDate.now(),
                ZoneId.systemDefault(),
                OffsetDateTime.now().getOffset(),
                DstChecker.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault())
        );
    }
}

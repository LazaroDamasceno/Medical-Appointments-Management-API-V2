package com.api.v2.doctors.domain;

import com.api.v2.doctors.domain.exposed.Doctor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public record DoctorAuditTrail(
        @BsonId
        ObjectId id,
        Doctor doctor,
        LocalDateTime createdAt,
        ZoneId createdAtZoneId,
        ZoneOffset createdAtZoneOffset
) {

    public static DoctorAuditTrail create(Doctor doctor) {
        return new DoctorAuditTrail(
                new ObjectId(),
                doctor,
                LocalDateTime.now(),
                ZoneId.systemDefault(),
                OffsetDateTime.now().getOffset()
        );
    }
}

package com.api.v2.doctors.domain;

import com.api.v2.common.DstChecker;
import com.api.v2.doctors.domain.exposed.Doctor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

public record DoctorAuditTrail(
        @Id
        String id,
        Doctor doctor,
        LocalDateTime createdAt,
        ZoneId createdAtZoneId,
        ZoneOffset createdAtZoneOffset,
        Boolean isCreatedDuringDST
) {

    public static DoctorAuditTrail of(Doctor doctor) {
        return new DoctorAuditTrail(
                UUID.randomUUID().toString(),
                doctor,
                LocalDateTime.now(),
                ZoneId.systemDefault(),
                OffsetDateTime.now().getOffset(),
                DstChecker.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault())
        );
    }
}

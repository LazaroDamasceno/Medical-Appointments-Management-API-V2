package com.api.v2.payments.domain;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.common.DstChecker;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Document
public record Payment(
        @Id
        String id,
        Card card,
        MedicalAppointment medicalAppointment,
        LocalDateTime paidAt,
        ZoneId paidAtZoneId,
        ZoneOffset paidAtZoneOffset,
        Boolean isPaymentDuringDST
) {

    public static Payment of(Card card, MedicalAppointment medicalAppointment) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
        return new Payment(
                UUID.randomUUID().toString(),
                card,
                medicalAppointment,
                localDateTime,
                zoneId,
                zoneOffset,
                DstChecker.isGivenDateTimeFollowingDst(localDateTime, zoneId)
        );
    }
}

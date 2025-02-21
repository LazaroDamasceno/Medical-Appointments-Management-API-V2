package com.api.v2.payments.domain;

import com.api.v2.cards.domain.Card;
import com.api.v2.common.DstCheckerUtil;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Document
public record Payment(
        @BsonId
        ObjectId id,
        Card card,
        MedicalAppointment medicalAppointment,
        LocalDateTime paidAt,
        ZoneId paidAtZoneId,
        ZoneOffset paidAtZoneOffset,
        boolean isPaymentDuringDST
) {

    public static Payment of(Card card, MedicalAppointment medicalAppointment) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
        return new Payment(
                new ObjectId(),
                card,
                medicalAppointment,
                localDateTime,
                zoneId,
                zoneOffset,
                DstCheckerUtil.isGivenDateTimeFollowingDst(localDateTime, zoneId)
        );
    }
}

package com.api.v2.payments.utils;

import com.api.v2.cards.util.CardResponseMapper;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.payments.domain.Payment;
import com.api.v2.payments.dtos.PaymentResponseDto;

public final class PaymentResponseMapper {

    public static PaymentResponseDto map(Payment payment) {
        return new PaymentResponseDto(
                payment.id(),
                CardResponseMapper.map(payment.card()),
                MedicalAppointmentResponseMapper.mapToDto(payment.medicalAppointment()),
                "%s%s[%s]".formatted(payment.paidAt(), payment.paidAtZoneOffset(), payment.paidAtZoneId())
        );
    }
}

package com.api.v2.payments.utils;

import com.api.v2.cards.utils.CardResponseMapper;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.payments.domain.Payment;
import com.api.v2.payments.dtos.PaymentResponseDto;

public final class PaymentResponseMapper {

    public static PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(
                payment.id(),
                CardResponseMapper.toDto(payment.card()),
                MedicalAppointmentResponseMapper.toDto(payment.medicalAppointment()),
                "%s%s[%s]".formatted(payment.paidAt(), payment.paidAtZoneOffset(), payment.paidAtZoneId())
        );
    }
}

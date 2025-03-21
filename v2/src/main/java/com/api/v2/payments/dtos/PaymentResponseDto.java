package com.api.v2.payments.dtos;

import com.api.v2.cards.dtos.CardResponseDto;
import com.api.v2.medical_appointments.dtos.SimplifiedMedicalAppointmentResponseDto;

public record PaymentResponseDto(
        String id,
        CardResponseDto card,
        SimplifiedMedicalAppointmentResponseDto medicalAppointment,
        String paidAt
) {
}

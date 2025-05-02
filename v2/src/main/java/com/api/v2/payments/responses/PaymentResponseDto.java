package com.api.v2.payments.responses;

import com.api.v2.cards.responses.CardResponseDto;
import com.api.v2.medical_appointments.responses.SimplifiedMedicalAppointmentResponseDto;

public record PaymentResponseDto(
        String id,
        CardResponseDto card,
        SimplifiedMedicalAppointmentResponseDto medicalAppointment,
        String paidAt
) {
}

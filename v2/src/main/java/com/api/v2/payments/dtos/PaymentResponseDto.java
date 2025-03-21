package com.api.v2.payments.dtos;

import com.api.v2.cards.dtos.CardResponseDto;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

public record PaymentResponseDto(
        String id,
        CardResponseDto card,
        MedicalAppointmentResponseResource medicalAppointment,
        String paidAt
) {
}

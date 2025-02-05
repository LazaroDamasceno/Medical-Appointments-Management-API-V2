package com.api.v2.medical_appointments.dtos;

import com.api.v2.customers.dtos.exposed.CustomerResponseDto;

public record SimplifiedMedicalAppointmentResponseDto(
        String id,
        CustomerResponseDto customer,
        String bookedAt,
        String canceledAt,
        String completedAt
) {
}

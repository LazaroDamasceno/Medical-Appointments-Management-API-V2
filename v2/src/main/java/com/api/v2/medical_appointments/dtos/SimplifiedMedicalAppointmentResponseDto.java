package com.api.v2.medical_appointments.dtos;

public record SimplifiedMedicalAppointmentResponseDto(
        String id,
        String bookedAt,
        String canceledAt,
        String completedAt
) {
}

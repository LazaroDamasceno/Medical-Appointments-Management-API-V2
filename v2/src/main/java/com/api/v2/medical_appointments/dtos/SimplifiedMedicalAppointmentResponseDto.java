package com.api.v2.medical_appointments.dtos;

import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.medical_appointments.enums.MedicalAppointmentType;

public record SimplifiedMedicalAppointmentResponseDto(
        String id,
        MedicalAppointmentType type,
        CustomerResponseDto customer,
        String bookedAt,
        String canceledAt,
        String completedAt
) {
}

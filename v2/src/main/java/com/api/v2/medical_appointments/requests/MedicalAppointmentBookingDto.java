package com.api.v2.medical_appointments.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalAppointmentBookingDto(
        String customerId,
        @NotNull LocalDateTime availableAt,
        String medicalSlotId
) {
}

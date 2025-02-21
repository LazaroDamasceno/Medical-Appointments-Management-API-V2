package com.api.v2.medical_appointments.dtos;

import com.api.v2.common.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalAppointmentBookingDto(
        @Id String customerId,
        @NotNull LocalDateTime availableAt,
        @Id String medicalSlotId
) {
}

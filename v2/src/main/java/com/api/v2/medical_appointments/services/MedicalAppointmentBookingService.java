package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

import java.time.LocalDateTime;

public interface MedicalAppointmentBookingService {
    MedicalAppointmentResponseResource book(String customerId, LocalDateTime availableAt, String medicalSlotId);
}

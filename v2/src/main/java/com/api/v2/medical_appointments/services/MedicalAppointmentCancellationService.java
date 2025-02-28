package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

public interface MedicalAppointmentCancellationService {
    MedicalAppointmentResponseResource cancelById(String customerId, String medicalAppointmentId);
}

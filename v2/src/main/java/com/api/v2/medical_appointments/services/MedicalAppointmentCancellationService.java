package com.api.v2.medical_appointments.services;

import com.api.v2.common.ResourceResponse;
import org.springframework.http.ResponseEntity;

public interface MedicalAppointmentCancellationService {
    ResponseEntity<ResourceResponse> cancelById(String customerId, String medicalAppointmentId);
}

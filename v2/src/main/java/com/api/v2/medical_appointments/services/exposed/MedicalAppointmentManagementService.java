package com.api.v2.medical_appointments.services.exposed;

import com.api.v2.common.ResourceResponse;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import org.springframework.http.ResponseEntity;

public interface MedicalAppointmentManagementService {
    MedicalAppointment complete(MedicalAppointment medicalAppointment);
    ResponseEntity<ResourceResponse> cancelById(String customerId, String medicalAppointmentId);
    MedicalAppointment cancel(MedicalAppointment medicalAppointment);
}

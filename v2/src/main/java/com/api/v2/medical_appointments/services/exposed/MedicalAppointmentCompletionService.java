package com.api.v2.medical_appointments.services.exposed;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;

public interface MedicalAppointmentCompletionService {
    MedicalAppointment complete(MedicalAppointment medicalAppointment);
}

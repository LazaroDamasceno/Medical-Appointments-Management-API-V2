package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.domain.MedicalAppointment;

public interface MedicalAppointmentCompletionService {
    MedicalAppointment complete(MedicalAppointment medicalAppointment);
}

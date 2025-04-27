package com.api.v2.medical_appointments.services.exposed;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;

public interface MedicalAppointmentUpdatingService {
    void set(MedicalAppointment medicalAppointment);
}

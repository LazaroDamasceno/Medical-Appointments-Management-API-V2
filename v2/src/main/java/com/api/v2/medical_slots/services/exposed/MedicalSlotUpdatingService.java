package com.api.v2.medical_slots.services.exposed;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;

public interface MedicalSlotUpdatingService {
    void set(MedicalSlot medicalSlot, MedicalAppointment medicalAppointment);
    void set(MedicalSlot medicalSlot);
}

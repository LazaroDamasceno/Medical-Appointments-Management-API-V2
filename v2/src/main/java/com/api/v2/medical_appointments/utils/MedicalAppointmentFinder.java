package com.api.v2.medical_appointments.utils;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.NonExistentMedicalAppointmentException;
import org.springframework.stereotype.Component;

@Component
public class MedicalAppointmentFinder {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public MedicalAppointmentFinder(MedicalAppointmentRepository medicalAppointmentRepository) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
    }

    public MedicalAppointment findById(String id) {
        return medicalAppointmentRepository
                .findById(id)
                .orElseThrow(() -> new NonExistentMedicalAppointmentException(id));
    }
}

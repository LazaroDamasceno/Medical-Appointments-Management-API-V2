package com.api.v2.medical_appointments.utils;

import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.NonExistentMedicalAppointmentException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class MedicalAppointmentFinderUtil {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public MedicalAppointmentFinderUtil(MedicalAppointmentRepository medicalAppointmentRepository) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
    }

    public MedicalAppointment findById(String id) {
        return medicalAppointmentRepository
                .findById(new ObjectId(id))
                .orElseThrow(() -> new NonExistentMedicalAppointmentException(id));
    }
}

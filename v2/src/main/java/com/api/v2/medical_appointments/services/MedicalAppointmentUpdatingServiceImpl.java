package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentUpdatingService;
import org.springframework.stereotype.Service;

@Service
public class MedicalAppointmentUpdatingServiceImpl implements MedicalAppointmentUpdatingService {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public MedicalAppointmentUpdatingServiceImpl(MedicalAppointmentRepository medicalAppointmentRepository) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
    }

    @Override
    public MedicalAppointment set(MedicalAppointment medicalAppointment) {
        medicalAppointment.markAsPaid();
        return medicalAppointmentRepository.save(medicalAppointment);
    }
}

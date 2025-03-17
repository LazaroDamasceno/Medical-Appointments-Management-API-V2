package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.ImmutableMedicalAppointmentStatusException;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentCompletionService;
import org.springframework.stereotype.Service;

@Service
public class MedicalAppointmentCompletionServiceImpl implements MedicalAppointmentCompletionService {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public MedicalAppointmentCompletionServiceImpl(MedicalAppointmentRepository medicalAppointmentRepository) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
    }

    @Override
    public MedicalAppointment complete(MedicalAppointment medicalAppointment) {
        onCanceledMedicalAppointment(medicalAppointment);
        onCompletedMedicalAppointment(medicalAppointment);
        medicalAppointment.markAsCompleted();
        return medicalAppointmentRepository.save(medicalAppointment);
    }

    private void onCanceledMedicalAppointment(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCanceledAt() != null && medicalAppointment.getCompletedAt() == null) {
            String message = "Medical appointment whose id is %s is already canceled.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }

    private void onCompletedMedicalAppointment(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCanceledAt() == null && medicalAppointment.getCompletedAt() != null) {
            String message = "Medical appointment whose id is %s is already completed.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }

}

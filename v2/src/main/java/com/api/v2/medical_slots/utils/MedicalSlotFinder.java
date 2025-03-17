package com.api.v2.medical_slots.utils;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.NonExistentMedicalSlotException;
import org.springframework.stereotype.Component;

@Component
public class MedicalSlotFinder {

    private final MedicalSlotRepository medicalSlotRepository;

    public MedicalSlotFinder(MedicalSlotRepository medicalSlotRepository) {
        this.medicalSlotRepository = medicalSlotRepository;
    }

    public MedicalSlot findById(String id) {
        return medicalSlotRepository
                .findById(id)
                .orElseThrow(() ->  new NonExistentMedicalSlotException(id));
    }

    public MedicalSlot findByMedicalAppointment(MedicalAppointment medicalAppointment) {
        return medicalSlotRepository
                .findAll()
                .stream()
                .filter(slot -> slot.getMedicalAppointment().getId().equals(medicalAppointment.getId()))
                .findFirst()
                .orElseThrow();
    }
}

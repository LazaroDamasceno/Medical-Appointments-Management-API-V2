package com.api.v2.medical_slots.utils;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.NonExistentMedicalSlotException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class MedicalSlotFinderUtil {

    private final MedicalSlotRepository medicalSlotRepository;

    public MedicalSlotFinderUtil(MedicalSlotRepository medicalSlotRepository) {
        this.medicalSlotRepository = medicalSlotRepository;
    }

    public MedicalSlot findById(String id) {
        return medicalSlotRepository
                .findById(new ObjectId(id))
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

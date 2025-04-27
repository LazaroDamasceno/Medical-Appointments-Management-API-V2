package com.api.v2.medical_slots.services;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.services.exposed.MedicalSlotUpdatingService;
import org.springframework.stereotype.Service;

@Service
public class MedicalSlotUpdatingServiceImpl implements MedicalSlotUpdatingService {

    private final MedicalSlotRepository medicalSlotRepository;

    public MedicalSlotUpdatingServiceImpl(MedicalSlotRepository medicalSlotRepository) {
        this.medicalSlotRepository = medicalSlotRepository;
    }

    @Override
    public void set(MedicalSlot medicalSlot, MedicalAppointment medicalAppointment) {
        medicalSlot.setMedicalAppointment(medicalAppointment);
        MedicalSlot savedSlot = medicalSlotRepository.save(medicalSlot);
    }

    @Override
    public void set(MedicalSlot medicalSlot) {
        MedicalSlot savedSlot = medicalSlotRepository.save(medicalSlot);
    }
}

package com.api.v2.medical_slots.services;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.services.exposed.MedicalSlotChangerService;
import org.springframework.stereotype.Service;

@Service
public class MedicalSlotChangerServiceImpl implements MedicalSlotChangerService {

    private final MedicalSlotRepository medicalSlotRepository;

    public MedicalSlotChangerServiceImpl(MedicalSlotRepository medicalSlotRepository) {
        this.medicalSlotRepository = medicalSlotRepository;
    }

    @Override
    public MedicalSlot set(MedicalSlot medicalSlot, MedicalAppointment medicalAppointment) {
        medicalSlot.setMedicalAppointment(medicalAppointment);
        return medicalSlotRepository.save(medicalSlot);
    }
}

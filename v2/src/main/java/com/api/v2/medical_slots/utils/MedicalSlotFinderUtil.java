package com.api.v2.medical_slots.utils;

import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.NonExistentMedicalSlotException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
}

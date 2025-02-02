package com.api.v2.medical_slots.services;

import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.ImmutableMedicalSlotStatusException;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import com.api.v2.medical_slots.utils.MedicalSlotResponseMapper;
import org.springframework.stereotype.Service;

@Service
public class MedicalSlotCancellationServiceImpl implements MedicalSlotCancellationService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;

    public MedicalSlotCancellationServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                              MedicalSlotFinderUtil medicalSlotFinderUtil
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
    }

    @Override
    public MedicalSlotResponseResource cancel(String id) {
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(id);
        onCanceledMedicalSlot(medicalSlot);
        onCompletedMedicalSlot(medicalSlot);
        medicalSlot.markAsCanceled();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        return MedicalSlotResponseMapper.mapToResource(canceledMedicalSlot);
    }

    private void onCanceledMedicalSlot(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() != null && medicalSlot.getCompletedAt() == null) {
            String message = "Medical slot whose id is %s is already canceled.".formatted(medicalSlot.getId().toString());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }

    private void onCompletedMedicalSlot(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() == null && medicalSlot.getCompletedAt() != null) {
            String message = "Medical slot whose id is %s is already completed.".formatted(medicalSlot.getId().toString());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }
}

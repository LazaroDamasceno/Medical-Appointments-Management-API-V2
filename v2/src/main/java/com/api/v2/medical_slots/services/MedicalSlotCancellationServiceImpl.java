package com.api.v2.medical_slots.services;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.ImmutableMedicalSlotStatusException;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import com.api.v2.medical_slots.utils.MedicalSlotResponseMapper;
import org.springframework.stereotype.Service;

@Service
public class MedicalSlotCancellationServiceImpl implements MedicalSlotCancellationService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final DoctorFinderUtil doctorFinderUtil;

    public MedicalSlotCancellationServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                              MedicalSlotFinderUtil medicalSlotFinderUtil,
                                              DoctorFinderUtil doctorFinderUtil
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
        this.doctorFinderUtil = doctorFinderUtil;
    }

    @Override
    public MedicalSlotResponseResource cancel(String medicalLicenseNumber, String id) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(id);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        onCanceledMedicalSlot(medicalSlot);
        onCompletedMedicalSlot(medicalSlot);
        medicalSlot.markAsCanceled();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        return MedicalSlotResponseMapper.mapToResource(canceledMedicalSlot);
    }

    private void onNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        if (!medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId().toString(), medicalSlot.getId().toString());
        }
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

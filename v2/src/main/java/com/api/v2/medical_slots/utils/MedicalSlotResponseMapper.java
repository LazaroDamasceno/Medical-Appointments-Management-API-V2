package com.api.v2.medical_slots.utils;

import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public class MedicalSlotResponseMapper {
    public static MedicalSlotResponseResource mapToResource(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() == null && medicalSlot.getCompletedAt() != null) {
            return new MedicalSlotResponseResource(
                    DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                    "%s%s[%s]".formatted(
                            medicalSlot.getAvailableAt(),
                            medicalSlot.getAvailableAtZoneId(),
                            medicalSlot.getAvailableAtZoneOffset()
                    ),
                    null,
                    "%s%s[%s]".formatted(
                            medicalSlot.getCompletedAt(),
                            medicalSlot.getCompletedAtZoneId(),
                            medicalSlot.getCompletedAtZoneOffset()
                    )
            );
        }
        if (medicalSlot.getCanceledAt() != null && medicalSlot.getCompletedAt() == null) {
            return new MedicalSlotResponseResource(
                    DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                    "%s%s[%s]".formatted(
                            medicalSlot.getAvailableAt(),
                            medicalSlot.getAvailableAtZoneId(),
                            medicalSlot.getAvailableAtZoneOffset()
                    ),
                    "%s%s[%s]".formatted(
                            medicalSlot.getCanceledAt(),
                            medicalSlot.getCanceledAtZoneId(),
                            medicalSlot.getCanceledAtZoneOffset()
                    ),
                    null
            );
        }
        return new MedicalSlotResponseResource(
                DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                "%s%s[%s]".formatted(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneId(),
                        medicalSlot.getAvailableAtZoneOffset()
                ),
                null,
                null
        );
    }
}

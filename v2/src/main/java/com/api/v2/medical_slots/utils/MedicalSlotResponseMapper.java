package com.api.v2.medical_slots.utils;

import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public class MedicalSlotResponseMapper {

    public static MedicalSlotResponseResource mapToResource(MedicalSlot slot) {
        if (slot.getCanceledAt() == null && slot.getCompletedAt() != null) {
            return mapToCompletedResource(slot);
        }
        if (slot.getCanceledAt() != null && slot.getCompletedAt() == null) {
            return mapToCanceledResourceWithoutMedicalAppointment(slot);
        }
        if (slot.getCanceledAt() != null && slot.getCompletedAt() == null && slot.getMedicalAppointment() != null) {
            return mapToCanceledResourceWithMedicalAppointment(slot);
        }
        if (slot.getCanceledAt() == null && slot.getCompletedAt() == null && slot.getMedicalAppointment() != null) {
            return mapToActiveResourceWithMedicalAppointment(slot);
        }
        return mapToActiveResourceWithoutMedicalAppointment(slot);
    }

    private static MedicalSlotResponseResource mapToCompletedResource(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId().toString(),
                DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                MedicalAppointmentResponseMapper.mapToDto(medicalSlot.getMedicalAppointment()),
                "%s%s[%s]".formatted(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                null,
                "%s%s[%s]".formatted(
                        medicalSlot.getCompletedAt(),
                        medicalSlot.getCompletedAtZoneOffset(),
                        medicalSlot.getCompletedAtZoneId()
                )
        );
    }

    private static MedicalSlotResponseResource mapToCanceledResourceWithoutMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId().toString(),
                DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                null,
                "%s%s[%s]".formatted(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                "%s%s[%s]".formatted(
                        medicalSlot.getCanceledAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getCanceledAtZoneId()
                ),
                null
        );
    }

    private static MedicalSlotResponseResource mapToCanceledResourceWithMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId().toString(),
                DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                MedicalAppointmentResponseMapper.mapToDto(medicalSlot.getMedicalAppointment()),
                "%s%s[%s]".formatted(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                "%s%s[%s]".formatted(
                        medicalSlot.getCanceledAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getCanceledAtZoneId()
                ),
                null
        );
    }

    private static MedicalSlotResponseResource mapToActiveResourceWithMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId().toString(),
                DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                MedicalAppointmentResponseMapper.mapToDto(medicalSlot.getMedicalAppointment()),
                "%s%s[%s]".formatted(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                null,
                null
        );
    }

    private static MedicalSlotResponseResource mapToActiveResourceWithoutMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId().toString(),
                DoctorResponseMapper.mapToResource(medicalSlot.getDoctor()),
                null,
                "%s%s[%s]".formatted(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                null,
                null
        );
    }
}

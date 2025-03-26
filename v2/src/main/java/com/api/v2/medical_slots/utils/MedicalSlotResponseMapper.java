package com.api.v2.medical_slots.utils;

import com.api.v2.common.DateTimeFormatter;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;

public final class MedicalSlotResponseMapper {

    public static MedicalSlotResponseResource toResource(MedicalSlot slot) {
        if (slot.getCanceledAt() == null && slot.getCompletedAt() != null) {
            return toCompletedResource(slot);
        }
        if (slot.getCanceledAt() != null && slot.getCompletedAt() == null) {
            return toCanceledResourceWithoutMedicalAppointment(slot);
        }
        if (slot.getCanceledAt() != null && slot.getCompletedAt() == null && slot.getMedicalAppointment() != null) {
            return toCanceledResourceWithMedicalAppointment(slot);
        }
        if (slot.getCanceledAt() == null && slot.getCompletedAt() == null && slot.getMedicalAppointment() != null) {
            return toActiveResourceWithMedicalAppointment(slot);
        }
        return toActiveResourceWithoutMedicalAppointment(slot);
    }

    private static MedicalSlotResponseResource toCompletedResource(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId(),
                DoctorResponseMapper.toResource(medicalSlot.getDoctor()),
                MedicalAppointmentResponseMapper.toDto(medicalSlot.getMedicalAppointment()),
                DateTimeFormatter.format(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                null,
                DateTimeFormatter.format(
                        medicalSlot.getCompletedAt(),
                        medicalSlot.getCompletedAtZoneOffset(),
                        medicalSlot.getCompletedAtZoneId()
                )
        );
    }

    private static MedicalSlotResponseResource toCanceledResourceWithoutMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId(),
                DoctorResponseMapper.toResource(medicalSlot.getDoctor()),
                null,
                DateTimeFormatter.format(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                DateTimeFormatter.format(
                        medicalSlot.getCanceledAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getCanceledAtZoneId()
                ),
                null
        );
    }

    private static MedicalSlotResponseResource toCanceledResourceWithMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId(),
                DoctorResponseMapper.toResource(medicalSlot.getDoctor()),
                MedicalAppointmentResponseMapper.toDto(medicalSlot.getMedicalAppointment()),
                DateTimeFormatter.format(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                DateTimeFormatter.format(
                        medicalSlot.getCanceledAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getCanceledAtZoneId()
                ),
                null
        );
    }

    private static MedicalSlotResponseResource toActiveResourceWithMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId(),
                DoctorResponseMapper.toResource(medicalSlot.getDoctor()),
                MedicalAppointmentResponseMapper.toDto(medicalSlot.getMedicalAppointment()),
                DateTimeFormatter.format(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                null,
                null
        );
    }

    private static MedicalSlotResponseResource toActiveResourceWithoutMedicalAppointment(MedicalSlot medicalSlot) {
        return new MedicalSlotResponseResource(
                medicalSlot.getId(),
                DoctorResponseMapper.toResource(medicalSlot.getDoctor()),
                null,
                DateTimeFormatter.format(
                        medicalSlot.getAvailableAt(),
                        medicalSlot.getAvailableAtZoneOffset(),
                        medicalSlot.getAvailableAtZoneId()
                ),
                null,
                null
        );
    }
}

package com.api.v2.medical_slots.dtos;

import com.api.v2.common.ErrorResponse;
import com.api.v2.doctors.dto.exposed.DoctorResponseDto;
import com.api.v2.medical_appointments.dtos.SimplifiedMedicalAppointmentResponseDto;

public class MedicalSlotResponseDto extends ErrorResponse {

    private final String id;
    private final DoctorResponseDto doctor;
    private final SimplifiedMedicalAppointmentResponseDto medicalAppointment;
    private final String availableAt;
    private final String canceledAt;
    private final String completedAt;

    public MedicalSlotResponseDto(String id,
                                  DoctorResponseDto doctor,
                                  SimplifiedMedicalAppointmentResponseDto medicalAppointment,
                                  String availableAt,
                                  String canceledAt,
                                  String completedAt
    ) {
        this.id = id;
        this.doctor = doctor;
        this.medicalAppointment = medicalAppointment;
        this.availableAt = availableAt;
        this.canceledAt = canceledAt;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public DoctorResponseDto getDoctor() {
        return doctor;
    }

    public String getAvailableAt() {
        return availableAt;
    }

    public String getCanceledAt() {
        return canceledAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public SimplifiedMedicalAppointmentResponseDto getMedicalAppointment() {
        return medicalAppointment;
    }
}

package com.api.v2.medical_slots.resources;

import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.medical_appointments.dtos.SimplifiedMedicalAppointmentResponseDto;
import org.springframework.hateoas.RepresentationModel;

public class MedicalSlotResponseResource extends RepresentationModel<MedicalSlotResponseResource> {

    private final String id;
    private final DoctorResponseResource doctor;
    private final SimplifiedMedicalAppointmentResponseDto medicalAppointment;
    private final String availableAt;
    private final String canceledAt;
    private final String completedAt;

    public MedicalSlotResponseResource(String id,
                                       DoctorResponseResource doctor,
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

    public DoctorResponseResource getDoctor() {
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

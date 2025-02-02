package com.api.v2.medical_slots.resources;

import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.hateoas.RepresentationModel;

public class MedicalSlotResponseResource extends RepresentationModel<MedicalSlotResponseResource> {

    private DoctorResponseResource doctor;
    private String availableAt;
    private String canceledAt;
    private String completedAt;

    public MedicalSlotResponseResource(DoctorResponseResource doctor,
                                       String availableAt,
                                       String canceledAt,
                                       String completedAt
    ) {
        this.doctor = doctor;
        this.availableAt = availableAt;
        this.canceledAt = canceledAt;
        this.completedAt = completedAt;
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
}

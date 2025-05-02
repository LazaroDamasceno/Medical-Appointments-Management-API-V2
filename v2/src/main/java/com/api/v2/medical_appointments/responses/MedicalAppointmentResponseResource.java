package com.api.v2.medical_appointments.responses;

import com.api.v2.customers.responses.CustomerResponseDto;
import com.api.v2.doctors.responses.DoctorResponseResource;
import com.api.v2.medical_appointments.enums.MedicalAppointmentType;
import org.springframework.hateoas.RepresentationModel;

public class MedicalAppointmentResponseResource extends RepresentationModel<MedicalAppointmentResponseResource> {

    private String id;
    private MedicalAppointmentType type;
    private CustomerResponseDto customer;
    private DoctorResponseResource doctor;
    private String bookedAt;
    private String canceledAt;
    private String completedAt;

    public MedicalAppointmentResponseResource(String id,
                                              MedicalAppointmentType type,
                                              CustomerResponseDto customer,
                                              DoctorResponseResource doctor,
                                              String bookedAt,
                                              String canceledAt,
                                              String completedAt
    ) {
        this.id = id;
        this.type = type;
        this.customer = customer;
        this.doctor = doctor;
        this.bookedAt = bookedAt;
        this.canceledAt = canceledAt;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public CustomerResponseDto getCustomer() {
        return customer;
    }

    public DoctorResponseResource getDoctor() {
        return doctor;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public String getCanceledAt() {
        return canceledAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public MedicalAppointmentType getType() {
        return type;
    }
}

package com.api.v2.medical_appointments.dtos;

import com.api.v2.common.ErrorResponse;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.doctors.dto.exposed.DoctorResponseDto;

public class MedicalAppointmentResponseDto extends ErrorResponse {

    private final String id;
    private final CustomerResponseDto customer;
    private final DoctorResponseDto doctor;
    private final String bookedAt;
    private final String canceledAt;
    private final String completedAt;

    public MedicalAppointmentResponseDto(String id,
                                         CustomerResponseDto customer,
                                         DoctorResponseDto doctor,
                                         String bookedAt,
                                         String canceledAt,
                                         String completedAt) {
        this.id = id;
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

    public DoctorResponseDto getDoctor() {
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
}

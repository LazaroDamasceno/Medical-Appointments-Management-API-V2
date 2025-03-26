package com.api.v2.medical_appointments.utils;

import com.api.v2.common.DateTimeFormatter;
import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.dtos.SimplifiedMedicalAppointmentResponseDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

public final class MedicalAppointmentResponseMapper {

    public static MedicalAppointmentResponseResource toResource(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCompletedAt() != null && medicalAppointment.getCanceledAt() == null) {
            return new MedicalAppointmentResponseResource(
                    medicalAppointment.getId(),
                    CustomerResponseMapper.toDto(medicalAppointment.getCustomer()),
                    DoctorResponseMapper.toResource(medicalAppointment.getDoctor()),
                    DateTimeFormatter.format(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    null,
                    DateTimeFormatter.format(
                            medicalAppointment.getCompletedAt(),
                            medicalAppointment.getCompletedAtZoneOffset(),
                            medicalAppointment.getCompletedAtZoneId()
                    )
            );
        }
        if (medicalAppointment.getCompletedAt() == null && medicalAppointment.getCanceledAt() != null) {
            return new MedicalAppointmentResponseResource(
                    medicalAppointment.getId(),
                    CustomerResponseMapper.toDto(medicalAppointment.getCustomer()),
                    DoctorResponseMapper.toResource(medicalAppointment.getDoctor()),
                    DateTimeFormatter.format(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    DateTimeFormatter.format(
                            medicalAppointment.getCanceledAt(),
                            medicalAppointment.getCanceledAtZoneOffset(),
                            medicalAppointment.getCanceledAtZoneId()
                    ),
                    null
            );
        }
        return new MedicalAppointmentResponseResource(
                medicalAppointment.getId(),
                CustomerResponseMapper.toDto(medicalAppointment.getCustomer()),
                DoctorResponseMapper.toResource(medicalAppointment.getDoctor()),
                DateTimeFormatter.format(
                        medicalAppointment.getBookedAt(),
                        medicalAppointment.getBookedAtZoneOffset(),
                        medicalAppointment.getBookedAtZoneId()
                ),
                null,
                null
        );
    }

    public static SimplifiedMedicalAppointmentResponseDto toDto(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCompletedAt() == null && medicalAppointment.getCanceledAt() != null) {
            return new SimplifiedMedicalAppointmentResponseDto(
                    medicalAppointment.getId(),
                    CustomerResponseMapper.toDto(medicalAppointment.getCustomer()),
                    DateTimeFormatter.format(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    DateTimeFormatter.format(
                            medicalAppointment.getCanceledAt(),
                            medicalAppointment.getCanceledAtZoneOffset(),
                            medicalAppointment.getCanceledAtZoneId()
                    ),
                    null
            );
        }
        if (medicalAppointment.getCompletedAt() != null && medicalAppointment.getCanceledAt() == null) {
            return new SimplifiedMedicalAppointmentResponseDto(
                    medicalAppointment.getId(),
                    CustomerResponseMapper.toDto(medicalAppointment.getCustomer()),
                    DateTimeFormatter.format(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    null,
                    DateTimeFormatter.format(
                            medicalAppointment.getCompletedAt(),
                            medicalAppointment.getCompletedAtZoneOffset(),
                            medicalAppointment.getCompletedAtZoneId()
                    )
            );
        }
        return new SimplifiedMedicalAppointmentResponseDto(
                medicalAppointment.getId(),
                CustomerResponseMapper.toDto(medicalAppointment.getCustomer()),
                DateTimeFormatter.format(
                        medicalAppointment.getBookedAt(),
                        medicalAppointment.getBookedAtZoneOffset(),
                        medicalAppointment.getBookedAtZoneId()
                ),
                null,
                null
        );
    }
}

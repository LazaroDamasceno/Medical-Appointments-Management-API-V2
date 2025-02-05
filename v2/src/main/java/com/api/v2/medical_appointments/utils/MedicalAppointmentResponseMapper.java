package com.api.v2.medical_appointments.utils;

import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.dtos.SimplifiedMedicalAppointmentResponseDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

public class MedicalAppointmentResponseMapper {

    public static MedicalAppointmentResponseResource mapToResource(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCompletedAt() != null && medicalAppointment.getCanceledAt() == null) {
            return new MedicalAppointmentResponseResource(
                    medicalAppointment.getId().toString(),
                    CustomerResponseMapper.mapToDto(medicalAppointment.getCustomer()),
                    DoctorResponseMapper.mapToResource(medicalAppointment.getDoctor()),
                    "%s%s[%s]".formatted(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    null,
                    "%s%s[%s]".formatted(
                            medicalAppointment.getCompletedAt(),
                            medicalAppointment.getCompletedAtZoneOffset(),
                            medicalAppointment.getCompletedAtZoneId()
                    )
            );
        }
        if (medicalAppointment.getCompletedAt() == null && medicalAppointment.getCanceledAt() != null) {
            return new MedicalAppointmentResponseResource(
                    medicalAppointment.getId().toString(),
                    CustomerResponseMapper.mapToDto(medicalAppointment.getCustomer()),
                    DoctorResponseMapper.mapToResource(medicalAppointment.getDoctor()),
                    "%s%s[%s]".formatted(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    "%s%s[%s]".formatted(
                            medicalAppointment.getCanceledAt(),
                            medicalAppointment.getCanceledAtZoneOffset(),
                            medicalAppointment.getCanceledAtZoneId()
                    ),
                    null
            );
        }
        return new MedicalAppointmentResponseResource(
                medicalAppointment.getId().toString(),
                CustomerResponseMapper.mapToDto(medicalAppointment.getCustomer()),
                DoctorResponseMapper.mapToResource(medicalAppointment.getDoctor()),
                "%s%s[%s]".formatted(
                        medicalAppointment.getBookedAt(),
                        medicalAppointment.getBookedAtZoneOffset(),
                        medicalAppointment.getBookedAtZoneId()
                ),
                null,
                null
        );
    }

    public static SimplifiedMedicalAppointmentResponseDto mapToDto(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCompletedAt() == null && medicalAppointment.getCanceledAt() != null) {
            return new SimplifiedMedicalAppointmentResponseDto(
                    medicalAppointment.getId().toString(),
                    CustomerResponseMapper.mapToDto(medicalAppointment.getCustomer()),
                    "%s%s[%s]".formatted(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    "%s%s[%s]".formatted(
                            medicalAppointment.getCanceledAt(),
                            medicalAppointment.getCanceledAtZoneOffset(),
                            medicalAppointment.getCanceledAtZoneId()
                    ),
                    null
            );
        }
        if (medicalAppointment.getCompletedAt() != null && medicalAppointment.getCanceledAt() == null) {
            return new SimplifiedMedicalAppointmentResponseDto(
                    medicalAppointment.getId().toString(),
                    CustomerResponseMapper.mapToDto(medicalAppointment.getCustomer()),
                    "%s%s[%s]".formatted(
                            medicalAppointment.getBookedAt(),
                            medicalAppointment.getBookedAtZoneOffset(),
                            medicalAppointment.getBookedAtZoneId()
                    ),
                    null,
                    "%s%s[%s]".formatted(
                            medicalAppointment.getCompletedAt(),
                            medicalAppointment.getCompletedAtZoneOffset(),
                            medicalAppointment.getCompletedAtZoneId()
                    )
            );
        }
        return new SimplifiedMedicalAppointmentResponseDto(
                medicalAppointment.getId().toString(),
                CustomerResponseMapper.mapToDto(medicalAppointment.getCustomer()),
                "%s%s[%s]".formatted(
                        medicalAppointment.getBookedAt(),
                        medicalAppointment.getBookedAtZoneOffset(),
                        medicalAppointment.getBookedAtZoneId()
                ),
                null,
                null
        );
    }
}

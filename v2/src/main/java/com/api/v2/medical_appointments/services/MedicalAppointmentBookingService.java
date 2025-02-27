package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentResponseDto;

public interface MedicalAppointmentBookingService {
    MedicalAppointmentResponseDto book(MedicalAppointmentBookingDto bookingDto);
}

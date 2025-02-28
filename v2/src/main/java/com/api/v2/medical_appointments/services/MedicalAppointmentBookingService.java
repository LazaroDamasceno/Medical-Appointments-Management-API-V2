package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

public interface MedicalAppointmentBookingService {
    MedicalAppointmentResponseResource book(MedicalAppointmentBookingDto bookingDto);
}

package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import org.springframework.http.ResponseEntity;

public interface MedicalAppointmentBookingService {
    ResponseEntity<MedicalAppointmentResponseResource> book(MedicalAppointmentBookingDto bookingDto);
}

package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.requests.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.responses.MedicalAppointmentResponseResource;
import org.springframework.http.ResponseEntity;

public interface MedicalAppointmentBookingService {
    ResponseEntity<MedicalAppointmentResponseResource> privateInsurance(MedicalAppointmentBookingDto bookingDto);
    ResponseEntity<MedicalAppointmentResponseResource> publicInsurance(MedicalAppointmentBookingDto bookingDto);
    ResponseEntity<MedicalAppointmentResponseResource> paidByPatient(MedicalAppointmentBookingDto bookingDto);
}

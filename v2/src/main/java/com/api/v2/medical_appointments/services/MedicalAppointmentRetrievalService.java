package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.responses.MedicalAppointmentResponseResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalAppointmentRetrievalService {
    ResponseEntity<MedicalAppointmentResponseResource> findById(String customerId, String medicalAppointmentId);
    ResponseEntity<List<MedicalAppointmentResponseResource>> findAllByCustomer(String customerId);
    ResponseEntity<List<MedicalAppointmentResponseResource>> findAll();
}

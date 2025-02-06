package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

import java.util.List;

public interface MedicalAppointmentRetrievalService {
    MedicalAppointmentResponseResource findById(String customerId, String medicalAppointmentId);
    List<MedicalAppointmentResponseResource> findAllByCustomer(String customerId);
    List<MedicalAppointmentResponseResource> findAll();
}

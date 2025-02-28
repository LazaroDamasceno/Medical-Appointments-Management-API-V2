package com.api.v2.medical_appointments.services;

import com.api.v2.common.Response;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;

import java.util.List;

public interface MedicalAppointmentRetrievalService {
    Response<MedicalAppointmentResponseResource> findById(String customerId, String medicalAppointmentId);
    Response<List<MedicalAppointmentResponseResource>> findAllByCustomer(String customerId);
    Response<List<MedicalAppointmentResponseResource>> findAll();
}

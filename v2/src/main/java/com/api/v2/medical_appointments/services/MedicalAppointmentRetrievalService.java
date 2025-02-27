package com.api.v2.medical_appointments.services;

import com.api.v2.medical_appointments.dtos.MedicalAppointmentResponseDto;

import java.util.List;

public interface MedicalAppointmentRetrievalService {
    MedicalAppointmentResponseDto findById(String customerId, String medicalAppointmentId);
    List<MedicalAppointmentResponseDto> findAllByCustomer(String customerId);
    List<MedicalAppointmentResponseDto> findAll();
}

package com.api.v2.payments.services;

import com.api.v2.payments.dtos.PaymentResponseDto;

public interface MedicalAppointmentService {
    PaymentResponseDto payPrivateInsurance(String customerId, String medicalAppointmentId, String cardId, double price);
    PaymentResponseDto payPublicInsurance(String customerId, String medicalAppointmentId, String cardId);
    PaymentResponseDto payPaidByPatient(String customerId, String medicalAppointmentId, String cardId, double price);
}

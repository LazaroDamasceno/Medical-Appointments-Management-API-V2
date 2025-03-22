package com.api.v2.payments.services;

import com.api.v2.payments.dtos.PaymentResponseDto;

public interface MedicalAppointmentPaymentService {
    PaymentResponseDto payPrivateInsurance(String medicalAppointmentId, String cardId, double price);
    PaymentResponseDto payPaidByPatient(String medicalAppointmentId, String cardId, double price);
}

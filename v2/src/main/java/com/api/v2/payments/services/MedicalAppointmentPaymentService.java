package com.api.v2.payments.services;

import com.api.v2.payments.dtos.PaymentResponseDto;

public interface MedicalAppointmentPaymentService {
    PaymentResponseDto pay(String medicalAppointmentId, String cardId, double price);
}

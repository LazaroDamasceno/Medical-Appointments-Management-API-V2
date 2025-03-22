package com.api.v2.payments.controllers;

import com.api.v2.payments.dtos.PaymentResponseDto;
import com.api.v2.payments.services.MedicalAppointmentPaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/payments")
public class PaymentController {

    private final MedicalAppointmentPaymentService paymentService;

    public PaymentController(MedicalAppointmentPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("private-insurance/{medicalAppointmentId}/{cardId}/{price}")
    public PaymentResponseDto payPrivateInsurance(@PathVariable String medicalAppointmentId,
                                                  @PathVariable String cardId,
                                                  @PathVariable double price
    ) {
        return paymentService.payPrivateInsurance(medicalAppointmentId, cardId, price);
    }

    @PostMapping("paid-by-patient/{medicalAppointmentId}/{cardId}/{price}")
    public PaymentResponseDto payPaidByPatient(@PathVariable String medicalAppointmentId,
                                               @PathVariable String cardId,
                                               @PathVariable double price
    ) {
        return paymentService.payPaidByPatient(medicalAppointmentId, cardId, price);
    }
}

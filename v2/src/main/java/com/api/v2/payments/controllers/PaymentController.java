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

    @PostMapping("{medicalAppointmentId}/{cardId}/{price}")
    public PaymentResponseDto pay(@PathVariable String medicalAppointmentId,
                                                  @PathVariable String cardId,
                                                  @PathVariable double price
    ) {
        return paymentService.pay(medicalAppointmentId, cardId, price);
    }
}

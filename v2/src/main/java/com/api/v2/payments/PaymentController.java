package com.api.v2.payments;

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

    @PostMapping("private-insurance/{customerId}/{medicalAppointmentId}/{cardId}/{price}")
    public PaymentResponseDto payPrivateInsurance(@PathVariable String customerId,
                                                  @PathVariable String medicalAppointmentId,
                                                  @PathVariable String cardId,
                                                  @PathVariable double price
    ) {
        return paymentService.payPrivateInsurance(customerId, medicalAppointmentId, cardId, price);
    }

    @PostMapping("public-insurance/{customerId}/{medicalAppointmentId}/{cardId}/{price}")
    public PaymentResponseDto payPublicInsurance(@PathVariable String customerId,
                                                 @PathVariable String medicalAppointmentId,
                                                 @PathVariable String cardId
    ) {
        return paymentService.payPublicInsurance(customerId, medicalAppointmentId, cardId);
    }

    @PostMapping("paid-by-patient/{customerId}/{medicalAppointmentId}/{cardId}/{price}")
    public PaymentResponseDto payPaidByPatient(@PathVariable String customerId,
                                               @PathVariable String medicalAppointmentId,
                                               @PathVariable String cardId,
                                               @PathVariable double price
    ) {
        return paymentService.payPaidByPatient(customerId, medicalAppointmentId, cardId, price);
    }
}

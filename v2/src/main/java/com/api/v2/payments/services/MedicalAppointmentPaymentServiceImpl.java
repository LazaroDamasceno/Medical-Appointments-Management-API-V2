package com.api.v2.payments.services;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.util.CardFinder;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinder;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.exceptions.ImmutableMedicalAppointmentStatusException;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinder;
import com.api.v2.payments.domain.Payment;
import com.api.v2.payments.domain.PaymentRepository;
import com.api.v2.payments.dtos.PaymentResponseDto;
import com.api.v2.payments.utils.PaymentResponseMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MedicalAppointmentPaymentServiceImpl implements MedicalAppointmentPaymentService {

    private final CustomerFinder customerFinder;
    private final MedicalAppointmentFinder medicalAppointmentFinder;
    private final CardFinder cardFinder;
    private final PaymentRepository paymentRepository;

    public MedicalAppointmentPaymentServiceImpl(CustomerFinder customerFinder,
                                                MedicalAppointmentFinder medicalAppointmentFinder,
                                                CardFinder cardFinder,
                                                PaymentRepository paymentRepository
    ) {
        this.customerFinder = customerFinder;
        this.medicalAppointmentFinder = medicalAppointmentFinder;
        this.cardFinder = cardFinder;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponseDto payPrivateInsurance(String customerId,
                                                  String medicalAppointmentId,
                                                  String cardId,
                                                  double price
    ) {
        return pay(customerId, medicalAppointmentId, cardId, BigDecimal.valueOf(price));
    }

    @Override
    public PaymentResponseDto payPublicInsurance(String customerId,
                                                 String medicalAppointmentId,
                                                 String cardId
    ) {
        double zeroedPrice = 0.0;
        return pay(customerId, medicalAppointmentId, cardId, BigDecimal.valueOf(zeroedPrice));
    }

    @Override
    public PaymentResponseDto payPaidByPatient(String customerId,
                                               String medicalAppointmentId,
                                               String cardId,
                                               double price
    ) {
        return pay(customerId, medicalAppointmentId, cardId, BigDecimal.valueOf(price));
    }

    private PaymentResponseDto pay(String customerId,
                                   String medicalAppointmentId,
                                   String cardId,
                                   BigDecimal price
    ) {
        Customer customer = customerFinder.findById(customerId);
        MedicalAppointment medicalAppointment = medicalAppointmentFinder.findById(medicalAppointmentId);
        Card card = cardFinder.findById(cardId);
        validate(medicalAppointment, customer);
        Payment payment = Payment.of(card, price, medicalAppointment);
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseMapper.map(savedPayment);
    }

    private void validate(MedicalAppointment medicalAppointment, Customer customer) {

        if (medicalAppointment.getCustomer().getId().equals(customer.getId())) {
            String message = """
                   Customer whose id is %s is not associated with the medical appointment whose id is  %s
            """.formatted(customer.getId(), medicalAppointment.getId());
            throw new InaccessibleMedicalAppointmentException(message);
        }

        if (medicalAppointment.getCanceledAt() != null && medicalAppointment.getCompletedAt() == null) {
            String message = "Medical appointment whose id is %s is already canceled.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }


        if (medicalAppointment.getCanceledAt() == null && medicalAppointment.getCompletedAt() == null) {
            String message = "Medical appointment whose id is %s is cannot be paid, due to it's active.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }

        if (medicalAppointment.getPaidAt() != null) {
            String message = "Medical appointment whose id is %s is already paid..".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }

}

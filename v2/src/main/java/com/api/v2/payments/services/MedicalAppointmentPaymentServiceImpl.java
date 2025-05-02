package com.api.v2.payments.services;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.utils.CardFinder;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.enums.MedicalAppointmentType;
import com.api.v2.medical_appointments.exceptions.ImmutableMedicalAppointmentStatusException;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentUpdatingService;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinder;
import com.api.v2.payments.domain.Payment;
import com.api.v2.payments.domain.PaymentRepository;
import com.api.v2.payments.responses.PaymentResponseDto;
import com.api.v2.payments.exceptions.IllegalChargingException;
import com.api.v2.payments.utils.PaymentResponseMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MedicalAppointmentPaymentServiceImpl implements MedicalAppointmentPaymentService {

    private final MedicalAppointmentFinder medicalAppointmentFinder;
    private final CardFinder cardFinder;
    private final PaymentRepository paymentRepository;
    private final MedicalAppointmentUpdatingService medicalAppointmentUpdatingService;

    public MedicalAppointmentPaymentServiceImpl(MedicalAppointmentFinder medicalAppointmentFinder,
                                                CardFinder cardFinder,
                                                PaymentRepository paymentRepository,
                                                MedicalAppointmentUpdatingService medicalAppointmentUpdatingService
    ) {
        this.medicalAppointmentFinder = medicalAppointmentFinder;
        this.cardFinder = cardFinder;
        this.paymentRepository = paymentRepository;
        this.medicalAppointmentUpdatingService = medicalAppointmentUpdatingService;
    }

    @Override
    public PaymentResponseDto pay(String medicalAppointmentId,
                                   String cardId,
                                   double price
    ) {
        MedicalAppointment medicalAppointment = medicalAppointmentFinder.findById(medicalAppointmentId);
        Card card = cardFinder.findById(cardId);
        validate(medicalAppointment);
        medicalAppointmentUpdatingService.set(medicalAppointment);
        Payment payment = Payment.of(card, BigDecimal.valueOf(price), medicalAppointment);
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseMapper.toDto(savedPayment);
    }

    private void validate(MedicalAppointment medicalAppointment) {

        if (medicalAppointment.getType().equals(MedicalAppointmentType.PUBLIC_INSURANCE)) {
            throw new IllegalChargingException(medicalAppointment.getId());
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
            String message = "Medical appointment whose id is %s is already paid.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }
}

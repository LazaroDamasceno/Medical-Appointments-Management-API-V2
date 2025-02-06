package com.api.v2.medical_appointments.services;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.ImmutableMedicalAppointmentStatusException;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinderUtil;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalAppointmentCancellationServiceImpl implements MedicalAppointmentCancellationService {

    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final CustomerFinderUtil customerFinderUtil;
    private final MedicalAppointmentFinderUtil medicalAppointmentFinderUtil;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final MedicalSlotRepository medicalSlotRepository;

    public MedicalAppointmentCancellationServiceImpl(MedicalAppointmentRepository medicalAppointmentRepository,
                                                     CustomerFinderUtil customerFinderUtil,
                                                     MedicalAppointmentFinderUtil medicalAppointmentFinderUtil,
                                                     MedicalSlotFinderUtil medicalSlotFinderUtil,
                                                     MedicalSlotRepository medicalSlotRepository
    ) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.customerFinderUtil = customerFinderUtil;
        this.medicalAppointmentFinderUtil = medicalAppointmentFinderUtil;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
        this.medicalSlotRepository = medicalSlotRepository;
    }

    @Override
    public MedicalAppointmentResponseResource cancel(String customerId, String medicalAppointmentId) {
        MedicalAppointment medicalAppointment = medicalAppointmentFinderUtil.findById(medicalAppointmentId);
        Customer customer = customerFinderUtil.findById(customerId);
        onNonAssociatedMedicalAppointmentWithCustomer(medicalAppointment, customer);
        onCanceledMedicalAppointment(medicalAppointment);
        onCompletedMedicalAppointment(medicalAppointment);
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findByMedicalAppointment(medicalAppointment);
        medicalAppointment.markAsCanceled();
        MedicalAppointment canceledMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.setMedicalAppointment(null);
        medicalSlot.markAsCanceled();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        return MedicalAppointmentResponseMapper
                .mapToResource(canceledMedicalAppointment)
                .add(
                        linkTo(
                                methodOn(MedicalAppointmentController.class).cancel(
                                        customer.getId().toString(),
                                        canceledMedicalSlot.getId().toString()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findById(
                                        customer.getId().toString(),
                                        canceledMedicalSlot.getId().toString()
                                )
                        ).withRel("find_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId().toString())
                        ).withRel("find_medical_appointments_by_customer")
                );
    }

    private void onNonAssociatedMedicalAppointmentWithCustomer(MedicalAppointment medicalAppointment, Customer customer) {
        if (medicalAppointment.getCustomer().getId().equals(customer.getId())) {
            throw new InaccessibleMedicalAppointmentException(customer.getId().toString(), medicalAppointment.getId().toString());
        }
    }

    private void onCanceledMedicalAppointment(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCanceledAt() != null && medicalAppointment.getCompletedAt() == null) {
            String message = "Medical appointment whose id is %s is already canceled.".formatted(medicalAppointment.getId().toString());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }

    private void onCompletedMedicalAppointment(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCanceledAt() == null && medicalAppointment.getCompletedAt() != null) {
            String message = "Medical appointment whose id is %s is already completed.".formatted(medicalAppointment.getId().toString());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }
}

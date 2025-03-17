package com.api.v2.medical_appointments.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinder;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.ImmutableMedicalAppointmentStatusException;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentCancellationService;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinder;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.services.exposed.MedicalSlotUpdatingService;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalAppointmentCancellationServiceImpl implements MedicalAppointmentCancellationService {

    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final CustomerFinder customerFinder;
    private final MedicalAppointmentFinder medicalAppointmentFinder;
    private final MedicalSlotFinder medicalSlotFinder;
    private final MedicalSlotUpdatingService medicalSlotUpdatingService;

    public MedicalAppointmentCancellationServiceImpl(MedicalAppointmentRepository medicalAppointmentRepository,
                                                     CustomerFinder customerFinder,
                                                     MedicalAppointmentFinder medicalAppointmentFinder,
                                                     MedicalSlotFinder medicalSlotFinder,
                                                     MedicalSlotRepository medicalSlotRepository, MedicalSlotUpdatingService medicalSlotUpdatingService
    ) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.customerFinder = customerFinder;
        this.medicalAppointmentFinder = medicalAppointmentFinder;
        this.medicalSlotFinder = medicalSlotFinder;
        this.medicalSlotUpdatingService = medicalSlotUpdatingService;
    }

    @Override
    public ResponseEntity<ResourceResponse> cancelById(String customerId, String medicalAppointmentId) {
        MedicalAppointment medicalAppointment = medicalAppointmentFinder.findById(medicalAppointmentId);
        Customer customer = customerFinder.findById(customerId);
        onNonAssociatedMedicalAppointmentWithCustomer(medicalAppointment, customer);
        onCanceledMedicalAppointment(medicalAppointment);
        onCompletedMedicalAppointment(medicalAppointment);
        MedicalSlot medicalSlot = medicalSlotFinder.findByMedicalAppointment(medicalAppointment);
        medicalAppointment.markAsCanceled();
        MedicalAppointment canceledMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.markAsCanceled();
        MedicalSlot canceledMedicalSlot = medicalSlotUpdatingService.set(medicalSlot);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalAppointmentController.class).cancel(
                                        customer.getId(),
                                        canceledMedicalSlot.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findById(
                                        customer.getId(),
                                        canceledMedicalSlot.getId()
                                )
                        ).withRel("find_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId())
                        ).withRel("find_medical_appointments_by_customer")
                );
        return ResponseEntity.ok(responseResource);
    }

    @Override
    public MedicalAppointment cancel(MedicalAppointment medicalAppointment) {
        medicalAppointment.markAsCanceled();
        return medicalAppointmentRepository.save(medicalAppointment);
    }

    private void onNonAssociatedMedicalAppointmentWithCustomer(MedicalAppointment medicalAppointment, Customer customer) {
        if (medicalAppointment.getCustomer().getId().equals(customer.getId())) {
            String message = """
                   Customer whose id is %s is not associated with the medical appointment whose id is  %s
            """.formatted(customer.getId(), medicalAppointment.getId());
            throw new InaccessibleMedicalAppointmentException(message);
        }
    }

    private void onCanceledMedicalAppointment(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCanceledAt() != null && medicalAppointment.getCompletedAt() == null) {
            String message = "Medical appointment whose id is %s is already canceled.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }

    private void onCompletedMedicalAppointment(MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCanceledAt() == null && medicalAppointment.getCompletedAt() != null) {
            String message = "Medical appointment whose id is %s is already completed.".formatted(medicalAppointment.getId());
            throw new ImmutableMedicalAppointmentStatusException(message);
        }
    }
}

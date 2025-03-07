package com.api.v2.medical_appointments.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.common.Id;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.ImmutableMedicalAppointmentStatusException;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinderUtil;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResourceResponse> cancelById(@Id String customerId, @Id String medicalAppointmentId) {
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
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
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
        return ResponseEntity.ok(responseResource);
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

package com.api.v2.medical_appointments.services;

import com.api.v2.common.*;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
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
    public Response<MedicalAppointmentResponseResource> cancelById(@Id String customerId, @Id String medicalAppointmentId) {
        Response<MedicalAppointment> medicalAppointmentResponse = medicalAppointmentFinderUtil.findById(medicalAppointmentId);
        MedicalAppointment medicalAppointment = medicalAppointmentResponse.getData();
        Response<Customer> customerResponse = customerFinderUtil.findById(customerId);
        Customer customer = customerResponse.getData();
        if (isNonAssociatedMedicalAppointmentWithCustomer(medicalAppointment, customer)) {
            return onNonAssociatedMedicalAppointmentWithCustomer();
        }
        if (isCanceledMedicalAppointment(medicalAppointment)) {
            return onCanceledMedicalAppointment();
        }
        if (isCompletedMedicalAppointment(medicalAppointment)) {
            return onCompletedMedicalAppointment();
        }
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findByMedicalAppointment(medicalAppointment);
        medicalAppointment.markAsCanceled();
        MedicalAppointment canceledMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.setMedicalAppointment(null);
        medicalSlot.markAsCanceled();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        MedicalAppointmentResponseResource responseResource = MedicalAppointmentResponseMapper
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
        return SuccessfulResponse.success(responseResource);
    }

    private boolean isNonAssociatedMedicalAppointmentWithCustomer(MedicalAppointment medicalAppointment, Customer customer) {
        return medicalAppointment.getCustomer().getId().equals(customer.getId());
    }

    private boolean isCanceledMedicalAppointment(MedicalAppointment medicalAppointment) {
        return medicalAppointment.getCanceledAt() != null && medicalAppointment.getCompletedAt() == null;
    }

    private boolean isCompletedMedicalAppointment(MedicalAppointment medicalAppointment) {
        return medicalAppointment.getCanceledAt() == null && medicalAppointment.getCompletedAt() != null;
    }

    private Response<MedicalAppointmentResponseResource> onNonAssociatedMedicalAppointmentWithCustomer() {
        String errorType = "Inaccessible medical appointment.";
        String errorMessage = "Customer is not associated with medical appointment.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<MedicalAppointmentResponseResource> onCanceledMedicalAppointment() {
        String errorType = "Immutable medical appointment.";
        String errorMessage = "Medical appointment is already canceled.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<MedicalAppointmentResponseResource> onCompletedMedicalAppointment() {
        String errorType = "Immutable medical appointment.";
        String errorMessage = "Medical appointment is already completed.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

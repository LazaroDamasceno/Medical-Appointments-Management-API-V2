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
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalAppointmentRetrievalServiceImpl implements MedicalAppointmentRetrievalService {

    private final CustomerFinderUtil customerFinderUtil;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalAppointmentFinderUtil medicalAppointmentFinderUtil;

    public MedicalAppointmentRetrievalServiceImpl(CustomerFinderUtil customerFinderUtil,
                                                  MedicalAppointmentRepository medicalAppointmentRepository,
                                                  MedicalAppointmentFinderUtil medicalAppointmentFinderUtil
    ) {
        this.customerFinderUtil = customerFinderUtil;
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.medicalAppointmentFinderUtil = medicalAppointmentFinderUtil;
    }

    @Override
    public Response<MedicalAppointmentResponseResource> findById(@Id String customerId, @Id String medicalAppointmentId) {
        Response<Customer> customerResponse = customerFinderUtil.findById(customerId);
        Customer customer = customerResponse.getData();
        Response<MedicalAppointment> medicalAppointmentResponse = medicalAppointmentFinderUtil.findById(medicalAppointmentId);
        MedicalAppointment medicalAppointment = medicalAppointmentResponse.getData();
        if (isNonAssociatedMedicalAppointmentWithCustomer(customer, medicalAppointment)) {
            return onNonAssociatedMedicalAppointmentWithCustomer();
        }
        MedicalAppointmentResponseResource responseResource =  MedicalAppointmentResponseMapper
                .mapToResource(medicalAppointment)
                .add(
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findById(
                                        customer.getId().toString(),
                                        medicalAppointment.getId().toString()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).cancel(
                                        customer.getId().toString(),
                                        medicalAppointment.getId().toString()
                                )
                        ).withRel("cancel_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId().toString())
                        ).withRel("find_medical_appointments_by_customer")
                );
        return SuccessfulResponse.success(responseResource);
    }

    private boolean isNonAssociatedMedicalAppointmentWithCustomer(Customer customer, MedicalAppointment medicalAppointment) {
        return medicalAppointment.getCustomer().getId().equals(customer.getId());
    }

    private Response<MedicalAppointmentResponseResource> onNonAssociatedMedicalAppointmentWithCustomer() {
        String errorType = "Inaccessible medical appointment.";
        String errorMessage = "Customer is not associated with medical appointment.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    @Override
    public Response<List<MedicalAppointmentResponseResource>> findAllByCustomer(String customerId) {
        Response<Customer> customerResponse = customerFinderUtil.findById(customerId);
        Customer customer = customerResponse.getData();
        List<MedicalAppointmentResponseResource> list = medicalAppointmentRepository
                .findAll()
                .stream()
                .filter(medicalAppointment -> medicalAppointment.getCustomer().getId().equals(customer.getId()))
                .map(MedicalAppointmentResponseMapper::mapToResource)
                .peek(resource ->
                        resource.add(
                                linkTo(
                                        methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId().toString())
                                ).withSelfRel(),
                                linkTo(
                                        methodOn(MedicalAppointmentController.class).findById(
                                                customer.getId().toString(),
                                                resource.getId()
                                        )
                                ).withRel("find_medical_appointment_by_id"),
                                linkTo(
                                        methodOn(MedicalAppointmentController.class).cancel(
                                                customer.getId().toString(),
                                                resource.getId()
                                        )
                                ).withRel("cancel_medical_appointment_by_id")
                        )
                )
                .toList();
        return SuccessfulResponse.success(list);
    }

    @Override
    public Response<List<MedicalAppointmentResponseResource>> findAll() {
        List<MedicalAppointmentResponseResource> list = medicalAppointmentRepository
                .findAll()
                .stream()
                .map(MedicalAppointmentResponseMapper::mapToResource)
                .toList();
        return SuccessfulResponse.success(list);
    }
}

package com.api.v2.medical_appointments.services;

import com.api.v2.common.Id;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinderUtil;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MedicalAppointmentResponseResource> findById(@Id String customerId, @Id String medicalAppointmentId) {
        Customer customer = customerFinderUtil.findById(customerId);
        MedicalAppointment medicalAppointment = medicalAppointmentFinderUtil.findById(medicalAppointmentId);
        onNonAssociatedMedicalAppointmentWithCustomer(customer, medicalAppointment);
        MedicalAppointmentResponseResource responseResource = MedicalAppointmentResponseMapper
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
        return ResponseEntity.ok(responseResource);
    }

    private void onNonAssociatedMedicalAppointmentWithCustomer(Customer customer, MedicalAppointment medicalAppointment) {
        if (medicalAppointment.getCustomer().getId().equals(customer.getId())) {
            String message = """
                   Customer whose id is %s is not associated with the medical appointment whose id is  %s
            """.formatted(customer.getId(), medicalAppointment.getId());
            throw new InaccessibleMedicalAppointmentException(message);
        }
    }
    @Override
    public ResponseEntity<List<MedicalAppointmentResponseResource>> findAllByCustomer(String customerId) {
        Customer customer = customerFinderUtil.findById(customerId);
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
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<List<MedicalAppointmentResponseResource>> findAll() {
        List<MedicalAppointmentResponseResource> list = medicalAppointmentRepository
                .findAll()
                .stream()
                .map(MedicalAppointmentResponseMapper::mapToResource)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}

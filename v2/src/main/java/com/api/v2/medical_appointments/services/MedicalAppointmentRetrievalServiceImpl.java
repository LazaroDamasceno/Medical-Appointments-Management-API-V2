package com.api.v2.medical_appointments.services;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinder;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.responses.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalAppointmentRetrievalServiceImpl implements MedicalAppointmentRetrievalService {

    private final CustomerFinder customerFinder;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalAppointmentFinder medicalAppointmentFinder;

    public MedicalAppointmentRetrievalServiceImpl(CustomerFinder customerFinder,
                                                  MedicalAppointmentRepository medicalAppointmentRepository,
                                                  MedicalAppointmentFinder medicalAppointmentFinder
    ) {
        this.customerFinder = customerFinder;
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.medicalAppointmentFinder = medicalAppointmentFinder;
    }

    @Override
    public ResponseEntity<MedicalAppointmentResponseResource> findById(String customerId, String medicalAppointmentId) {
        Customer customer = customerFinder.findById(customerId);
        MedicalAppointment medicalAppointment = medicalAppointmentFinder.findById(medicalAppointmentId);
        onNonAssociatedMedicalAppointmentWithCustomer(customer, medicalAppointment);
        MedicalAppointmentResponseResource responseResource = medicalAppointment
                .toResource()
                .add(
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findById(
                                        customer.getId(),
                                        medicalAppointment.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).cancel(
                                        customer.getId(),
                                        medicalAppointment.getId()
                                )
                        ).withRel("cancel_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId())
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
        Customer customer = customerFinder.findById(customerId);
        List<MedicalAppointmentResponseResource> list = medicalAppointmentRepository
                .findAll()
                .stream()
                .filter(medicalAppointment -> medicalAppointment.getCustomer().getId().equals(customer.getId()))
                .map(MedicalAppointment::toResource)
                .peek(resource ->
                        resource.add(
                                linkTo(
                                        methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId())
                                ).withSelfRel(),
                                linkTo(
                                        methodOn(MedicalAppointmentController.class).findById(
                                                customer.getId(),
                                                resource.getId()
                                        )
                                ).withRel("find_medical_appointment_by_id"),
                                linkTo(
                                        methodOn(MedicalAppointmentController.class).cancel(
                                                customer.getId(),
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
                .map(MedicalAppointment::toResource)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}

package com.api.v2.medical_appointments.services;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.UnavailableMedicalAppointmentBookingDateTimeException;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalAppointmentBookingServiceImpl implements MedicalAppointmentBookingService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final CustomerFinderUtil customerFinderUtil;


    public MedicalAppointmentBookingServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                                MedicalAppointmentRepository medicalAppointmentRepository,
                                                MedicalSlotFinderUtil medicalSlotFinderUtil,
                                                CustomerFinderUtil customerFinderUtil
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
        this.customerFinderUtil = customerFinderUtil;
    }

    @Override
    public MedicalAppointmentResponseResource book(String customerId,
                                                   LocalDateTime availableAt,
                                                   String medicalSlotId
    ) {
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(medicalSlotId);
        Customer customer = customerFinderUtil.findById(customerId);
        Doctor doctor = medicalSlot.getDoctor();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetTime
                .ofInstant(availableAt.toInstant(ZoneOffset.UTC), zoneId)
                .getOffset();
        onDuplicatedBookingDateTime(customer, doctor, availableAt, zoneOffset, zoneId);
        MedicalAppointment medicalAppointment = MedicalAppointment.create(
                customer,
                doctor,
                availableAt,
                zoneId,
                zoneOffset
        );
        MedicalAppointment savedMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.setMedicalAppointment(medicalAppointment);
        MedicalSlot updtedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        return MedicalAppointmentResponseMapper
                .mapToResource(savedMedicalAppointment)
                .add(
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findById(
                                        customer.getId().toString(),
                                        savedMedicalAppointment.getId().toString()
                                )
                        ).withRel("find_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).cancel(
                                        customer.getId().toString(),
                                        savedMedicalAppointment.getId().toString()
                                )
                        ).withRel("cancel_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId().toString())
                        ).withRel("find_medical_appointments_by_customer")
                );
    }

    private void onDuplicatedBookingDateTime(Customer customer,
                                             Doctor doctor,
                                             LocalDateTime availableAt,
                                             ZoneOffset zoneOffset,
                                             ZoneId zoneId
    ) {
        boolean isGivenBookingDateTimeDuplicated = medicalAppointmentRepository
                .findAll()
                .stream()
                .anyMatch(appointment ->
                        appointment.getCustomer().getId().equals(customer.getId())
                        && appointment.getDoctor().getId().equals(doctor.getId())
                        && appointment.getBookedAt().equals(availableAt)
                        && appointment.getBookedAtZoneOffset().equals(zoneOffset)
                        && appointment.getBookedAtZoneId().equals(zoneId)
                );
        if (isGivenBookingDateTimeDuplicated) {
            throw new UnavailableMedicalAppointmentBookingDateTimeException();
        }
    }
}

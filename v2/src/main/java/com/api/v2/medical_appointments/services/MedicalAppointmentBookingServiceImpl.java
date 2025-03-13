package com.api.v2.medical_appointments.services;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.exceptions.UnavailableMedicalAppointmentBookingDateTimeException;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalAppointmentBookingServiceImpl implements MedicalAppointmentBookingService {

    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final CustomerFinderUtil customerFinderUtil;


    public MedicalAppointmentBookingServiceImpl(@Valid MedicalSlotRepository medicalSlotRepository,
                                                MedicalAppointmentRepository medicalAppointmentRepository,
                                                MedicalSlotFinderUtil medicalSlotFinderUtil,
                                                CustomerFinderUtil customerFinderUtil
    ) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
        this.customerFinderUtil = customerFinderUtil;
    }

    @Override
    public ResponseEntity<MedicalAppointmentResponseResource> book(@Valid MedicalAppointmentBookingDto bookingDto) {
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(bookingDto.medicalSlotId());
        Customer customer = customerFinderUtil.findById(bookingDto.medicalSlotId());
        Doctor doctor = medicalSlot.getDoctor();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetTime
                .ofInstant(bookingDto.availableAt().toInstant(ZoneOffset.UTC), zoneId)
                .getOffset();
        onBlockedBooking(medicalSlot, bookingDto.customerId());
        onDuplicatedBookingDateTime(customer, doctor, bookingDto.availableAt(), zoneOffset, zoneId);
        MedicalAppointment medicalAppointment = MedicalAppointment.of(
                customer,
                doctor,
                bookingDto.availableAt(),
                zoneId,
                zoneOffset
        );
        MedicalAppointment savedMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.setMedicalAppointment(medicalAppointment);
        MedicalAppointmentResponseResource responseResource = MedicalAppointmentResponseMapper
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
        return ResponseEntity.status(HttpStatus.CREATED).body(responseResource);
    }

    private void onBlockedBooking(MedicalSlot medicalSlot, String customerId) {
        if (medicalSlot.getDoctor().getPerson().getId().equals(new ObjectId(customerId))) {
            String message = "Customer cannot book a medical appointment which they're the related medical slot's doctor.";
            throw new InaccessibleMedicalAppointmentException(message);
        }
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

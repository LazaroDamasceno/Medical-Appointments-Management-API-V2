package com.api.v2.medical_appointments.services;

import com.api.v2.common.PastBookingDateException;
import com.api.v2.common.PastDateChecker;
import com.api.v2.common.UnavailableBookingDateTimeException;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinder;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.exceptions.UnavailableDoctorException;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.requests.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.enums.MedicalAppointmentType;
import com.api.v2.medical_appointments.exceptions.InaccessibleMedicalAppointmentException;
import com.api.v2.medical_appointments.responses.MedicalAppointmentResponseResource;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.services.exposed.MedicalSlotUpdatingService;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import jakarta.validation.Valid;
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

    private final MedicalSlotUpdatingService medicalAppointmentSetterService;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalSlotFinder medicalSlotFinder;
    private final CustomerFinder customerFinder;

    public MedicalAppointmentBookingServiceImpl(MedicalSlotUpdatingService medicalSlotUpdatingService,
                                                MedicalAppointmentRepository medicalAppointmentRepository,
                                                MedicalSlotFinder medicalSlotFinder,
                                                CustomerFinder customerFinder
    ) {
        this.medicalAppointmentSetterService = medicalSlotUpdatingService;
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.medicalSlotFinder = medicalSlotFinder;
        this.customerFinder = customerFinder;
    }


    @Override
    public ResponseEntity<MedicalAppointmentResponseResource> privateInsurance(@Valid MedicalAppointmentBookingDto bookingDto) {
        return bookAppointment(bookingDto, MedicalAppointmentType.PRIVATE_INSURANCE);
    }

    @Override
    public ResponseEntity<MedicalAppointmentResponseResource> publicInsurance(MedicalAppointmentBookingDto bookingDto) {
        return bookAppointment(bookingDto, MedicalAppointmentType.PUBLIC_INSURANCE);
    }

    @Override
    public ResponseEntity<MedicalAppointmentResponseResource> paidByPatient(MedicalAppointmentBookingDto bookingDto) {
        return bookAppointment(bookingDto, MedicalAppointmentType.PAID_BY_PATIENT);
    }

    private ResponseEntity<MedicalAppointmentResponseResource> bookAppointment(MedicalAppointmentBookingDto bookingDto,
                                                                               MedicalAppointmentType appointmentType
    ) {
        MedicalSlot medicalSlot = medicalSlotFinder.findById(bookingDto.medicalSlotId());
        Customer customer = customerFinder.findById(bookingDto.customerId());
        Doctor doctor = medicalSlot.getDoctor();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetTime
                .ofInstant(bookingDto.availableAt().toInstant(ZoneOffset.UTC), zoneId)
                .getOffset();
        validateBooking(
                medicalSlot,
                customer,
                doctor,
                bookingDto.availableAt(),
                zoneOffset,
                zoneId
        );
        MedicalAppointment medicalAppointment = MedicalAppointment.of(
                appointmentType,
                customer,
                doctor,
                bookingDto.availableAt(),
                zoneId,
                zoneOffset
        );
        MedicalAppointment savedMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalAppointmentSetterService.set(medicalSlot, medicalAppointment);
        MedicalAppointmentResponseResource responseResource = savedMedicalAppointment
                .toResource()
                .add(
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findById(
                                        customer.getId(),
                                        savedMedicalAppointment.getId()
                                )
                        ).withRel("find_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).cancel(
                                        customer.getId(),
                                        savedMedicalAppointment.getId()
                                )
                        ).withRel("cancel_medical_appointment_by_id"),
                        linkTo(
                                methodOn(MedicalAppointmentController.class).findAllByCustomer(customer.getId())
                        ).withRel("find_medical_appointments_by_customer")
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseResource);
    }

    private void validateBooking(
            MedicalSlot medicalSlot,
            Customer customer,
            Doctor doctor,
            LocalDateTime availableAt,
            ZoneOffset zoneOffset,
            ZoneId zoneId
    ) {
        if (PastDateChecker.isBeforeToday(availableAt.toLocalDate())) {
            throw new PastBookingDateException();
        }

        if (doctor.getTerminatedAt() != null) {
            throw new UnavailableDoctorException();
        }

        if (medicalSlot.getDoctor().getPerson().getId().equals(customer.getPerson().getId())) {
            String message = "Customer cannot book a medical appointment which they're the related medical slot's doctor.";
            throw new InaccessibleMedicalAppointmentException(message);
        }

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
            throw new UnavailableBookingDateTimeException();
        }
    }

}

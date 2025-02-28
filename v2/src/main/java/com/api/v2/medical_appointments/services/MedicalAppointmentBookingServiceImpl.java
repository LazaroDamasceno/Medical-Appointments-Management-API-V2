package com.api.v2.medical_appointments.services;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.common.SuccessfulResponse;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.controllers.MedicalAppointmentController;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import jakarta.validation.Valid;
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
    public Response<MedicalAppointmentResponseResource> book(@Valid MedicalAppointmentBookingDto bookingDto) {
        Response<MedicalSlot> medicalSlotResponse = medicalSlotFinderUtil.findById(bookingDto.medicalSlotId());
        MedicalSlot medicalSlot = medicalSlotResponse.getData();
        Response<Customer> customerResponse = customerFinderUtil.findById(bookingDto.medicalSlotId());
        Customer customer = customerResponse.getData();
        Doctor doctor = medicalSlot.getDoctor();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetTime
                .ofInstant(bookingDto.availableAt().toInstant(ZoneOffset.UTC), zoneId)
                .getOffset();
        if (isBookingDateTimeDuplicated(customer, doctor, bookingDto.availableAt(), zoneOffset, zoneId)) {
            return onDuplicatedBookingDateTime();
        }
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
        return SuccessfulResponse.success(Constants.CREATED_201, responseResource);
    }

    private boolean isBookingDateTimeDuplicated(Customer customer,
                                             Doctor doctor,
                                             LocalDateTime availableAt,
                                             ZoneOffset zoneOffset,
                                             ZoneId zoneId
    ) {
        return medicalAppointmentRepository
                .findAll()
                .stream()
                .anyMatch(appointment ->
                        appointment.getCustomer().getId().equals(customer.getId())
                        && appointment.getDoctor().getId().equals(doctor.getId())
                        && appointment.getBookedAt().equals(availableAt)
                        && appointment.getBookedAtZoneOffset().equals(zoneOffset)
                        && appointment.getBookedAtZoneId().equals(zoneId)
                );
    }

    private Response<MedicalAppointmentResponseResource> onDuplicatedBookingDateTime() {
        String errorType = "Unavailable booking datetime.";
        String errorMessage = "Given booking datetime is already associated with an active medical appointment.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

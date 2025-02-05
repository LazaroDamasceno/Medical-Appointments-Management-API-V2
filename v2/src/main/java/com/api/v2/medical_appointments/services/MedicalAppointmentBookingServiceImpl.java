package com.api.v2.medical_appointments.services;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_appointments.exceptions.UnavailableMedicalAppointmentBookingDateTimeException;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.utils.MedicalAppointmentResponseMapper;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class MedicalAppointmentBookingServiceImpl implements MedicalAppointmentBookingService {

    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final CustomerFinderUtil customerFinderUtil;

    public MedicalAppointmentBookingServiceImpl(MedicalAppointmentRepository medicalAppointmentRepository,
                                                MedicalSlotFinderUtil medicalSlotFinderUtil,
                                                CustomerFinderUtil customerFinderUtil
    ) {
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
        return MedicalAppointmentResponseMapper.mapToResource(savedMedicalAppointment);
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

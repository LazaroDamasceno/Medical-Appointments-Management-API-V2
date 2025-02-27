package com.api.v2.medical_slots.services;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.exceptions.UnavailableMedicalBookingDateTimeException;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotResponseMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotRegistrationServiceImpl implements MedicalSlotRegistrationService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final DoctorFinderUtil doctorFinderUtil;

    public MedicalSlotRegistrationServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                              DoctorFinderUtil doctorFinderUtil
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.doctorFinderUtil = doctorFinderUtil;
    }


    @Override
    public MedicalSlotResponseResource register(@Valid MedicalSlotRegistrationDto registrationDto) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(registrationDto.medicalLicenseNumber());
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetDateTime
                .ofInstant(registrationDto.availableAt().toInstant(ZoneOffset.UTC), zoneId)
                .getOffset();
        onDuplicatedBookingDateTime(doctor, registrationDto.availableAt(), zoneId, zoneOffset);
        MedicalSlot medicalSlot = MedicalSlot.of(doctor, registrationDto.availableAt(), zoneId, zoneOffset);
        MedicalSlot savedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        return MedicalSlotResponseMapper
                .mapToResource(savedMedicalSlot)
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(registrationDto.medicalLicenseNumber(), savedMedicalSlot.getId().toString())
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(registrationDto.medicalLicenseNumber())
                        ).withRel("find_medical_slot_by_doctor"),
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(registrationDto.medicalLicenseNumber(), savedMedicalSlot.getId().toString())
                        ).withRel("cancel_medical_slot_by_id")
                );
    }

    private void onDuplicatedBookingDateTime(Doctor doctor,
                                             LocalDateTime availableAt,
                                             ZoneId zoneId,
                                             ZoneOffset zoneOffset
    ) {
        boolean isGivenBookingDateTimeDuplicated = medicalSlotRepository
                .findAll()
                .stream()
                .anyMatch(slot -> slot.getDoctor().getId().equals(doctor.getId())
                        && slot.getAvailableAt().equals(availableAt)
                        && slot.getAvailableAtZoneId().equals(zoneId)
                        && slot.getAvailableAtZoneOffset().equals(zoneOffset)
                );
        if (isGivenBookingDateTimeDuplicated) {
            throw new UnavailableMedicalBookingDateTimeException();
        }
    }
}

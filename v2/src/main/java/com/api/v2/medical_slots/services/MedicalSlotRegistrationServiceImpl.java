package com.api.v2.medical_slots.services;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.common.SuccessfulResponse;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
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
    public Response<MedicalSlotResponseResource> register(@Valid MedicalSlotRegistrationDto registrationDto) {
        Response<Doctor> doctorResponse = doctorFinderUtil.findByMedicalLicenseNumber(registrationDto.medicalLicenseNumber());
        Doctor doctor = doctorResponse.getData();
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetDateTime
                .ofInstant(registrationDto.availableAt().toInstant(ZoneOffset.UTC), zoneId)
                .getOffset();
        if (isDuplicatedBookingDateTime(doctor, registrationDto.availableAt(), zoneId, zoneOffset)) {
            return onDuplicatedBookingDateTime();
        }
        MedicalSlot medicalSlot = MedicalSlot.of(doctor, registrationDto.availableAt(), zoneId, zoneOffset);
        MedicalSlot savedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        MedicalSlotResponseResource responseResource = MedicalSlotResponseMapper
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
        return SuccessfulResponse.success(Constants.CREATED_201, responseResource);
    }

    private boolean isDuplicatedBookingDateTime(Doctor doctor,
                                             LocalDateTime availableAt,
                                             ZoneId zoneId,
                                             ZoneOffset zoneOffset
    ) {
        return medicalSlotRepository
                .findAll()
                .stream()
                .anyMatch(slot -> slot.getDoctor().getId().equals(doctor.getId())
                        && slot.getAvailableAt().equals(availableAt)
                        && slot.getAvailableAtZoneId().equals(zoneId)
                        && slot.getAvailableAtZoneOffset().equals(zoneOffset)
                );
    }

    private Response<MedicalSlotResponseResource> onDuplicatedBookingDateTime() {
        String errorType = "Unavailable booking datetime.";
        String errorMessage = "Given booking datetime is associated with an active medical slot.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

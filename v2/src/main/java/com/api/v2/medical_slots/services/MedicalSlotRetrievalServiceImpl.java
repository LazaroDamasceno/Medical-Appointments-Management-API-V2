package com.api.v2.medical_slots.services;

import com.api.v2.common.*;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import com.api.v2.medical_slots.utils.MedicalSlotResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotRetrievalServiceImpl implements MedicalSlotRetrievalService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final DoctorFinderUtil doctorFinderUtil;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;

    public MedicalSlotRetrievalServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                           DoctorFinderUtil doctorFinderUtil,
                                           MedicalSlotFinderUtil medicalSlotFinderUtil
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.doctorFinderUtil = doctorFinderUtil;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
    }

    @Override
    public Response<MedicalSlotResponseResource> findById(@MLN String medicalLicenseNumber, @Id String slotId) {
        Response<Doctor> doctorResponse = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        Doctor doctor = doctorResponse.getData();
        Response<MedicalSlot> medicalSlotResponse = medicalSlotFinderUtil.findById(slotId);
        MedicalSlot medicalSlot = medicalSlotResponse.getData();
        if (isNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor)) {
            return onNonAssociatedMedicalSlotWithDoctor();
        }
        MedicalSlotResponseResource responseResource = MedicalSlotResponseMapper
                .mapToResource(medicalSlot)
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(medicalSlot.getDoctor().getMedicalLicenseNumber(), slotId)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalSlot.getDoctor().getMedicalLicenseNumber())
                        ).withRel("find_medical_slots_by_doctor"),
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(medicalSlot.getDoctor().getId().toString(), slotId)
                        ).withRel("cancel_medical_slot_by_id")
                );
        return SuccessfulResponse.success(responseResource);
    }

    private boolean isNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        return medicalSlot.getDoctor().getId().equals(doctor.getId());
    }

    private Response<MedicalSlotResponseResource> onNonAssociatedMedicalSlotWithDoctor() {
        String errorType = "Inaccessible medical slot.";
        String errorMessage = "Doctor not associated with medical slot.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    @Override
    public Response<List<MedicalSlotResponseResource>> findAllByDoctor(String medicalLicenseNumber) {
        Response<Doctor> doctorResponse = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        Doctor doctor = doctorResponse.getData();
        List<MedicalSlotResponseResource> list = medicalSlotRepository
                .findAll()
                .stream()
                .filter(slot -> slot.getDoctor().getId().equals(doctor.getId()))
                .map(MedicalSlotResponseMapper::mapToResource)
                .peek(slot -> slot.add(
                            linkTo(
                                    methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber)
                            ).withSelfRel(),
                            linkTo(
                                    methodOn(MedicalSlotController.class).findById(slot.getDoctor().getMedicalLicenseNumber(), slot.getId())
                            ).withRel("find_medical_slot_by_id"),
                            linkTo(
                                    methodOn(MedicalSlotController.class).cancel(slot.getDoctor().getMedicalLicenseNumber(), slot.getId())
                            ).withRel("cancel_medical_slot_by_id")
                    )
                )
                .toList();
        return SuccessfulResponse.success(list);
    }

    @Override
    public Response<List<MedicalSlotResponseResource>> findAll() {
        List<MedicalSlotResponseResource> list = medicalSlotRepository
                .findAll()
                .stream()
                .map(MedicalSlotResponseMapper::mapToResource)
                .toList();
        return SuccessfulResponse.success(list);
    }
}

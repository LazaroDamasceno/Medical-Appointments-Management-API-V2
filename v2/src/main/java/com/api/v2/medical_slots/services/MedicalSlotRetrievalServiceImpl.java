package com.api.v2.medical_slots.services;

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
    public MedicalSlotResponseResource findById(String id) {
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(id);
        return MedicalSlotResponseMapper
                .mapToResource(medicalSlot)
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(id)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findByDoctor(medicalSlot.getDoctor().getMedicalLicenseNumber())
                        ).withRel("find_medical_slots_by_doctor"),
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(medicalSlot.getDoctor().getId().toString(), id)
                        ).withRel("cancel_medical_slot_by_id")
                );
    }

    @Override
    public List<MedicalSlotResponseResource> findByDoctor(String medicalLicenseNumber) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        return medicalSlotRepository
                .findAll()
                .stream()
                .filter(slot -> slot.getDoctor().getId().equals(doctor.getId()))
                .map(MedicalSlotResponseMapper::mapToResource)
                .peek(slot -> slot.add(
                            linkTo(
                                    methodOn(MedicalSlotController.class).findByDoctor(medicalLicenseNumber)
                            ).withSelfRel(),
                            linkTo(
                                    methodOn(MedicalSlotController.class).cancel(slot.getDoctor().getMedicalLicenseNumber(), slot.getId())
                            ).withRel("cancel_medical_slot_by_id")
                    )
                )
                .toList();
    }

    @Override
    public List<MedicalSlotResponseResource> findAll() {
        return medicalSlotRepository
                .findAll()
                .stream()
                .map(MedicalSlotResponseMapper::mapToResource)
                .toList();
    }
}

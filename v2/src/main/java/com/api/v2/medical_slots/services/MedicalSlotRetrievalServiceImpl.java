package com.api.v2.medical_slots.services;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotRetrievalServiceImpl implements MedicalSlotRetrievalService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final DoctorFinder doctorFinder;
    private final MedicalSlotFinder medicalSlotFinder;

    public MedicalSlotRetrievalServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                           DoctorFinder doctorFinder,
                                           MedicalSlotFinder medicalSlotFinder
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.doctorFinder = doctorFinder;
        this.medicalSlotFinder = medicalSlotFinder;
    }

    @Override
    public ResponseEntity<MedicalSlotResponseResource> findById(String medicalLicenseNumber, String state, String slotId) {
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber, state);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(slotId);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        MedicalSlotResponseResource responseResource = medicalSlot
                .toResource()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, state, slotId)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber, state)
                        ).withRel("find_medical_slots_by_doctor"),
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(medicalLicenseNumber, state, slotId)
                        ).withRel("cancel_medical_slot_by_id")
                );
        return ResponseEntity.ok(responseResource);
    }

    private void onNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        if (medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId(), medicalSlot.getId());
        }
    }

    @Override
    public ResponseEntity<List<MedicalSlotResponseResource>> findAllByDoctor(String medicalLicenseNumber, String state) {
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber, state);
        List<MedicalSlotResponseResource> list = medicalSlotRepository
                .findAll()
                .stream()
                .filter(slot -> slot.getDoctor().getId().equals(doctor.getId()))
                .map(MedicalSlot::toResource)
                .peek(slot -> slot.add(
                            linkTo(
                                    methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber, state)
                            ).withSelfRel(),
                            linkTo(
                                    methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, state, slot.getId())
                            ).withRel("find_medical_slot_by_id"),
                            linkTo(
                                    methodOn(MedicalSlotController.class).cancel(medicalLicenseNumber, state, slot.getId())
                            ).withRel("cancel_medical_slot_by_id")
                    )
                )
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<List<MedicalSlotResponseResource>> findAll() {
        List<MedicalSlotResponseResource> list = medicalSlotRepository
                .findAll()
                .stream()
                .map(MedicalSlot::toResource)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}

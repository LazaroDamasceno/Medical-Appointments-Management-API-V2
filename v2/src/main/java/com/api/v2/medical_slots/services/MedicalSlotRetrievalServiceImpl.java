package com.api.v2.medical_slots.services;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.doctors.utils.MedicalLicenseNumberFormatter;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import com.api.v2.medical_slots.utils.MedicalSlotResponseMapper;
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
    public ResponseEntity<MedicalSlotResponseResource> findById(String medicalLicenseNumber, String medicalRegion, String slotId) {
        MedicalLicenseNumber doctorLicenseNumber = MedicalLicenseNumberFormatter.format(medicalRegion, medicalRegion);
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(doctorLicenseNumber);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(slotId);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        MedicalSlotResponseResource responseResource = MedicalSlotResponseMapper
                .mapToResource(medicalSlot)
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, medicalRegion, slotId)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber, medicalRegion)
                        ).withRel("find_medical_slots_by_doctor"),
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(medicalLicenseNumber, medicalRegion, slotId)
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
    public ResponseEntity<List<MedicalSlotResponseResource>> findAllByDoctor(String medicalLicenseNumber, String medicalRegion) {
        MedicalLicenseNumber doctorLicenseNumber = MedicalLicenseNumberFormatter.format(medicalRegion, medicalRegion);
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(doctorLicenseNumber);
        List<MedicalSlotResponseResource> list = medicalSlotRepository
                .findAll()
                .stream()
                .filter(slot -> slot.getDoctor().getId().equals(doctor.getId()))
                .map(MedicalSlotResponseMapper::mapToResource)
                .peek(slot -> slot.add(
                            linkTo(
                                    methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber, medicalRegion)
                            ).withSelfRel(),
                            linkTo(
                                    methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, medicalRegion, slot.getId())
                            ).withRel("find_medical_slot_by_id"),
                            linkTo(
                                    methodOn(MedicalSlotController.class).cancel(medicalLicenseNumber, medicalRegion, slot.getId())
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
                .map(MedicalSlotResponseMapper::mapToResource)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}

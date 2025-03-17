package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.common.MLN;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.services.MedicalAppointmentCancellationService;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.ImmutableMedicalSlotStatusException;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotCancellationServiceImpl implements MedicalSlotCancellationService {

    private final MedicalAppointmentCancellationService medicalAppointmentCancellationService;
    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalSlotFinder medicalSlotFinder;
    private final DoctorFinder doctorFinder;

    public MedicalSlotCancellationServiceImpl(MedicalAppointmentCancellationService medicalAppointmentCancellationService,
                                              MedicalSlotRepository medicalSlotRepository,
                                              MedicalSlotFinder medicalSlotFinder,
                                              DoctorFinder doctorFinder
    ) {
        this.medicalAppointmentCancellationService = medicalAppointmentCancellationService;
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalSlotFinder = medicalSlotFinder;
        this.doctorFinder = doctorFinder;
    }

    @Override
    public ResponseEntity<ResourceResponse> cancelById(@MLN String medicalLicenseNumber, String id) {
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(id);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        onCanceledMedicalSlot(medicalSlot);
        onCompletedMedicalSlot(medicalSlot);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        if (medicalAppointment == null) {
            return response(medicalSlot);
        }
        return response(medicalSlot, medicalAppointment);
    }

    private ResponseEntity<ResourceResponse> response(MedicalSlot medicalSlot) {
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        String medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(
                                        medicalLicenseNumber,
                                        medicalSlot.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(
                                        medicalLicenseNumber,
                                        medicalSlot.getId()
                                )
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber)
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    private ResponseEntity<ResourceResponse> response(MedicalSlot medicalSlot, MedicalAppointment medicalAppointment) {
        MedicalAppointment canceledMedicalAppointment = medicalAppointmentCancellationService.cancel(medicalAppointment);
        String medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(
                                        medicalLicenseNumber,
                                        medicalSlot.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(
                                        medicalLicenseNumber,
                                        medicalSlot.getId()
                                )
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber)
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    private void onNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        if (!medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId(), medicalSlot.getId());
        }
    }

    private void onCanceledMedicalSlot(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() != null && medicalSlot.getCompletedAt() == null) {
            String message = "Medical slot whose id is %s is already canceled.".formatted(medicalSlot.getId());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }

    private void onCompletedMedicalSlot(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() == null && medicalSlot.getCompletedAt() != null) {
            String message = "Medical slot whose id is %s is already completed.".formatted(medicalSlot.getId());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }
}

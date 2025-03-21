package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentManagementService;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.ImmutableMedicalSlotStatusException;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotManagementServiceImpl implements MedicalSlotManagementService {

    private final MedicalAppointmentManagementService medicalAppointmentManagementService;
    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalSlotFinder medicalSlotFinder;
    private final DoctorFinder doctorFinder;

    public MedicalSlotManagementServiceImpl(MedicalAppointmentManagementService medicalAppointmentManagementService,
                                            MedicalSlotRepository medicalSlotRepository,
                                            MedicalSlotFinder medicalSlotFinder,
                                            DoctorFinder doctorFinder
    ) {
        this.medicalAppointmentManagementService = medicalAppointmentManagementService;
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalSlotFinder = medicalSlotFinder;
        this.doctorFinder = doctorFinder;
    }

    @Override
    public ResponseEntity<ResourceResponse> cancelById(String medicalLicenseNumber, String state, String id) {
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber, state);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(id);
        validateCancellation(medicalSlot, doctor);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        return buildCancellationResponse(medicalSlot, medicalAppointment);
    }

    private ResponseEntity<ResourceResponse> buildCancellationResponse(MedicalSlot medicalSlot, MedicalAppointment medicalAppointment) {
        MedicalLicenseNumber medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        if (medicalAppointment != null) {
            MedicalAppointment canceledMedicalAppointment = medicalAppointmentManagementService.cancel(medicalAppointment);
        }
        medicalSlot.markAsCanceled();
        medicalSlot.setMedicalAppointment(null);
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.state().toString(),
                                        medicalSlot.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.state().toString(),
                                        medicalSlot.getId()
                                )
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.state().toString()
                                )
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    @Override
    public ResponseEntity<ResourceResponse> completeById(String medicalLicenseNumber, String state, String slotId) {
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber, state);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(slotId);
        validateCancellation(medicalSlot, doctor);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        if (medicalAppointment == null) {
            String message = "There's no active medical appointment associated with the current medical slot.";
            throw new ImmutableMedicalSlotStatusException(message);
        }
        medicalAppointment.markAsCompleted();
        MedicalAppointment completedMedicalAppointment = medicalAppointmentManagementService.complete(medicalAppointment);
        medicalSlot.markAsCompleted(completedMedicalAppointment);
        MedicalSlot completedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).complete(medicalLicenseNumber, state, slotId)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, state, slotId)
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber, state)
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    private void validateCancellation(MedicalSlot medicalSlot, Doctor doctor) {
        if (!medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId(), medicalSlot.getId());
        }

        if (medicalSlot.getCanceledAt() != null && medicalSlot.getCompletedAt() == null) {
            String message = "Medical slot whose id is %s is already canceled.".formatted(medicalSlot.getId());
            throw new ImmutableMedicalSlotStatusException(message);
        }

        if (medicalSlot.getCanceledAt() == null && medicalSlot.getCompletedAt() != null) {
            String message = "Medical slot whose id is %s is already completed.".formatted(medicalSlot.getId());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }

}

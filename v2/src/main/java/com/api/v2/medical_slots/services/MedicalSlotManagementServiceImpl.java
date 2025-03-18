package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.doctors.utils.MedicalLicenseNumberFormatter;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentCancellationService;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentCompletionService;
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

    private final MedicalAppointmentCancellationService medicalAppointmentCancellationService;
    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalSlotFinder medicalSlotFinder;
    private final DoctorFinder doctorFinder;
    private final MedicalAppointmentCompletionService medicalAppointmentCompletionService;

    public MedicalSlotManagementServiceImpl(MedicalAppointmentCancellationService medicalAppointmentCancellationService,
                                            MedicalSlotRepository medicalSlotRepository,
                                            MedicalSlotFinder medicalSlotFinder,
                                            DoctorFinder doctorFinder,
                                            MedicalAppointmentCompletionService medicalAppointmentCompletionService
    ) {
        this.medicalAppointmentCancellationService = medicalAppointmentCancellationService;
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalSlotFinder = medicalSlotFinder;
        this.doctorFinder = doctorFinder;
        this.medicalAppointmentCompletionService = medicalAppointmentCompletionService;
    }

    @Override
    public ResponseEntity<ResourceResponse> cancelById(String medicalLicenseNumber, String medicalRegion, String id) {
        MedicalLicenseNumber doctorLicenseNumber = MedicalLicenseNumberFormatter.format(medicalRegion, medicalRegion);
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(doctorLicenseNumber);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(id);
        validate(medicalSlot, doctor);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        if (medicalAppointment == null) {
            return cancellationResponse(medicalSlot);
        }
        return cancellationResponse(medicalSlot, medicalAppointment);
    }

    private ResponseEntity<ResourceResponse> cancellationResponse(MedicalSlot medicalSlot) {
        medicalSlot.markAsCanceled();
        medicalSlot.setMedicalAppointment(null);
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        MedicalLicenseNumber medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.medicalRegion().toString(),
                                        medicalSlot.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.medicalRegion().toString(),
                                        medicalSlot.getId()
                                )
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.medicalRegion().toString()
                                )
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    private ResponseEntity<ResourceResponse> cancellationResponse(MedicalSlot medicalSlot, MedicalAppointment medicalAppointment) {
        MedicalAppointment canceledMedicalAppointment = medicalAppointmentCancellationService.cancel(medicalAppointment);
        MedicalLicenseNumber medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        medicalSlot.markAsCanceled();
        medicalSlot.setMedicalAppointment(null);
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.medicalRegion().toString(),
                                        medicalSlot.getId()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.medicalRegion().toString(),
                                        medicalSlot.getId()
                                )
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(
                                        medicalLicenseNumber.licenseNumber(),
                                        medicalLicenseNumber.medicalRegion().toString()
                                )
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    @Override
    public ResponseEntity<ResourceResponse> completeById(String medicalLicenseNumber, String medicalRegion, String slotId) {
        MedicalLicenseNumber doctorLicenseNumber = MedicalLicenseNumberFormatter.format(medicalRegion, medicalRegion);
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(doctorLicenseNumber);
        MedicalSlot medicalSlot = medicalSlotFinder.findById(slotId);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        medicalAppointment.markAsCompleted();
        MedicalAppointment completedMedicalAppointment = medicalAppointmentCompletionService.complete(medicalAppointment);
        medicalSlot.markAsCompleted(completedMedicalAppointment);
        MedicalSlot completedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).complete(medicalLicenseNumber, medicalRegion, slotId)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, medicalRegion, slotId)
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber, medicalRegion)
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    private void validate(MedicalSlot medicalSlot, Doctor doctor) {
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        onCanceledMedicalSlot(medicalSlot);
        onCompletedMedicalSlot(medicalSlot);
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

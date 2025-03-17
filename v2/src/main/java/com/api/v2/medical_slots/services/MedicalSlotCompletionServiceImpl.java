package com.api.v2.medical_slots.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.common.MLN;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import com.api.v2.medical_appointments.services.MedicalAppointmentCompletionService;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.exposed.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.utils.MedicalSlotFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotCompletionServiceImpl implements MedicalSlotCompletionService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalAppointmentCompletionService medicalAppointmentCompletionService;
    private final MedicalSlotFinder medicalSlotFinder;
    private final DoctorFinder doctorFinder;

    public MedicalSlotCompletionServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                            MedicalAppointmentCompletionService medicalAppointmentCompletionService,
                                            MedicalSlotFinder medicalSlotFinder,
                                            DoctorFinder doctorFinder
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalAppointmentCompletionService = medicalAppointmentCompletionService;
        this.medicalSlotFinder = medicalSlotFinder;
        this.doctorFinder = doctorFinder;
    }

    @Override
    public ResponseEntity<ResourceResponse> completeById(@MLN String medicalLicenseNumber, String slotId) {
        MedicalSlot medicalSlot = medicalSlotFinder.findById(slotId);
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber);
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
                                methodOn(MedicalSlotController.class).complete(medicalLicenseNumber, slotId)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(medicalLicenseNumber, slotId)
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber)
                        ).withRel("find_medical_slots_by_doctor")
                );
        return ResponseEntity.ok(responseResource);
    }

    private void onNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        if (medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId(), medicalSlot.getId());
        }
    }
}

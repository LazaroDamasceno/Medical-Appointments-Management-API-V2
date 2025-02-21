package com.api.v2.medical_slots.services;

import com.api.v2.common.Id;
import com.api.v2.common.MLN;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.ImmutableMedicalSlotStatusException;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.utils.MedicalSlotFinderUtil;
import com.api.v2.medical_slots.utils.MedicalSlotResponseMapper;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicalSlotCancellationServiceImpl implements MedicalSlotCancellationService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final DoctorFinderUtil doctorFinderUtil;
    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public MedicalSlotCancellationServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                              MedicalSlotFinderUtil medicalSlotFinderUtil,
                                              DoctorFinderUtil doctorFinderUtil,
                                              MedicalAppointmentRepository medicalAppointmentRepository
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
        this.doctorFinderUtil = doctorFinderUtil;
        this.medicalAppointmentRepository = medicalAppointmentRepository;
    }

    @Override
    public MedicalSlotResponseResource cancelById(@MLN String medicalLicenseNumber, @Id String id) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(id);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        onCanceledMedicalSlot(medicalSlot);
        onCompletedMedicalSlot(medicalSlot);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        if (medicalAppointment == null) {
            return response(medicalSlot);
        }
        medicalAppointment.markAsCanceled();
        MedicalAppointment canceledMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.setMedicalAppointment(canceledMedicalAppointment);
        medicalSlot.markAsCanceled();
        return response(medicalSlot);
    }

    private MedicalSlotResponseResource response(MedicalSlot medicalSlot) {
        String medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        return MedicalSlotResponseMapper
                .mapToResource(canceledMedicalSlot)
                .add(
                        linkTo(
                                methodOn(MedicalSlotController.class).cancel(
                                        medicalLicenseNumber,
                                        medicalSlot.getId().toString()
                                )
                        ).withSelfRel(),
                        linkTo(
                                methodOn(MedicalSlotController.class).findById(
                                        medicalLicenseNumber,
                                        medicalSlot.getId().toString()
                                )
                        ).withRel("find_medical_slot_by_id"),
                        linkTo(
                                methodOn(MedicalSlotController.class).findAllByDoctor(medicalLicenseNumber)
                        ).withRel("find_medical_slots_by_doctor")
                );
    }

    private void onNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        if (!medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId().toString(), medicalSlot.getId().toString());
        }
    }

    private void onCanceledMedicalSlot(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() != null && medicalSlot.getCompletedAt() == null) {
            String message = "Medical slot whose id is %s is already canceled.".formatted(medicalSlot.getId().toString());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }

    private void onCompletedMedicalSlot(MedicalSlot medicalSlot) {
        if (medicalSlot.getCanceledAt() == null && medicalSlot.getCompletedAt() != null) {
            String message = "Medical slot whose id is %s is already completed.".formatted(medicalSlot.getId().toString());
            throw new ImmutableMedicalSlotStatusException(message);
        }
    }
}

package com.api.v2.medical_slots.services;

import com.api.v2.common.*;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
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
    public Response<MedicalSlotResponseResource> cancelById(@MLN String medicalLicenseNumber, @Id String id) {
        Response<Doctor> doctorResponse = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        Doctor doctor = doctorResponse.getData();
        Response<MedicalSlot> medicalSlotResponse = medicalSlotFinderUtil.findById(id);
        MedicalSlot medicalSlot = medicalSlotResponse.getData();
        if (isNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor)) {
            return onNonAssociatedMedicalSlotWithDoctor();
        }
        if (isCanceledMedicalSlot(medicalSlot)) {
            return onCanceledMedicalSlot();
        }
        if (isCompletedMedicalSlot(medicalSlot)) {
            return onCompletedMedicalSlot();
        }
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

    private Response<MedicalSlotResponseResource> response(MedicalSlot medicalSlot) {
        String medicalLicenseNumber = medicalSlot.getDoctor().getMedicalLicenseNumber();
        MedicalSlot canceledMedicalSlot = medicalSlotRepository.save(medicalSlot);
        MedicalSlotResponseResource responseResource = MedicalSlotResponseMapper
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
        return SuccessfulResponse.success(responseResource);
    }

    private boolean isNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        return !medicalSlot.getDoctor().getId().equals(doctor.getId());
    }

    private boolean isCanceledMedicalSlot(MedicalSlot medicalSlot) {
        return medicalSlot.getCanceledAt() != null && medicalSlot.getCompletedAt() == null;
    }

    private boolean isCompletedMedicalSlot(MedicalSlot medicalSlot) {
        return medicalSlot.getCanceledAt() == null && medicalSlot.getCompletedAt() != null;
    }

    private Response<MedicalSlotResponseResource> onNonAssociatedMedicalSlotWithDoctor() {
        String errorType = "Inaccessible medical slot.";
        String errorMessage = "Doctor not associated with medical slot.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<MedicalSlotResponseResource> onCanceledMedicalSlot() {
        String errorType = "Immutable medical slot.";
        String errorMessage = "Medical slot is already canceled.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<MedicalSlotResponseResource> onCompletedMedicalSlot() {
        String errorType = "Immutable medical slot.";
        String errorMessage = "Medical slot is already completed.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

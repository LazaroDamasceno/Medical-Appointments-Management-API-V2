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
public class MedicalSlotCompletionServiceImpl implements MedicalSlotCompletionService {

    private final MedicalSlotRepository medicalSlotRepository;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalSlotFinderUtil medicalSlotFinderUtil;
    private final DoctorFinderUtil doctorFinderUtil;

    public MedicalSlotCompletionServiceImpl(MedicalSlotRepository medicalSlotRepository,
                                            MedicalAppointmentRepository medicalAppointmentRepository,
                                            MedicalSlotFinderUtil medicalSlotFinderUtil,
                                            DoctorFinderUtil doctorFinderUtil
    ) {
        this.medicalSlotRepository = medicalSlotRepository;
        this.medicalAppointmentRepository = medicalAppointmentRepository;
        this.medicalSlotFinderUtil = medicalSlotFinderUtil;
        this.doctorFinderUtil = doctorFinderUtil;
    }

    @Override
    public Response<MedicalSlotResponseResource> completeById(@MLN String medicalLicenseNumber, @Id String slotId) {
        Response<MedicalSlot> medicalSlotResponse = medicalSlotFinderUtil.findById(slotId);
        MedicalSlot medicalSlot = medicalSlotResponse.getData();
        Response<Doctor> doctorResponse = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        Doctor doctor = doctorResponse.getData();
        if (isNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor)) {
            return onNonAssociatedMedicalSlotWithDoctor();
        }
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        medicalAppointment.markAsCompleted();
        MedicalAppointment completedMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.markAsCompleted();
        MedicalSlot completedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        medicalSlot.setMedicalAppointment(completedMedicalAppointment);
        MedicalSlotResponseResource responseResource = MedicalSlotResponseMapper
                .mapToResource(completedMedicalSlot)
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
        return SuccessfulResponse.success(responseResource);
    }

    private boolean isNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        return !medicalSlot.getDoctor().getId().equals(doctor.getId());
    }

    private Response<MedicalSlotResponseResource> onNonAssociatedMedicalSlotWithDoctor() {
        String errorType = "Inaccessible medical slot.";
        String errorMessage = "Doctor not associated with medical slot.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

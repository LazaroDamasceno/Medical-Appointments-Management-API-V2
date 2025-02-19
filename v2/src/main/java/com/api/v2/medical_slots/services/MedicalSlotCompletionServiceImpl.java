package com.api.v2.medical_slots.services;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v2.medical_slots.controllers.MedicalSlotController;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import com.api.v2.medical_slots.exceptions.InaccessibleMedicalSlotException;
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
    public MedicalSlotResponseResource completeById(String medicalLicenseNumber, String slotId) {
        MedicalSlot medicalSlot = medicalSlotFinderUtil.findById(slotId);
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        onNonAssociatedMedicalSlotWithDoctor(medicalSlot, doctor);
        MedicalAppointment medicalAppointment = medicalSlot.getMedicalAppointment();
        medicalAppointment.markAsCompleted();
        MedicalAppointment completedMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        medicalSlot.markAsCompleted();
        MedicalSlot completedMedicalSlot = medicalSlotRepository.save(medicalSlot);
        medicalSlot.setMedicalAppointment(completedMedicalAppointment);
        return MedicalSlotResponseMapper
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
    }

    private void onNonAssociatedMedicalSlotWithDoctor(MedicalSlot medicalSlot, Doctor doctor) {
        if (medicalSlot.getDoctor().getId().equals(doctor.getId())) {
            throw new InaccessibleMedicalSlotException(doctor.getId().toString(), medicalSlot.getId().toString());
        }
    }
}

package com.api.v2.doctors.services;

import com.api.v2.common.MLN;
import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorAuditTrail;
import com.api.v2.doctors.domain.DoctorAuditTrailRepository;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.exceptions.ImmutableDoctorStatusException;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DoctorRehireServiceImpl implements DoctorRehireService {

    private final DoctorFinderUtil doctorFinderUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorAuditTrailRepository doctorAuditTrailRepository;

    public DoctorRehireServiceImpl(DoctorFinderUtil doctorFinderUtil,
                                        DoctorRepository doctorRepository,
                                        DoctorAuditTrailRepository doctorAuditTrailRepository
    ) {
        this.doctorFinderUtil = doctorFinderUtil;
        this.doctorRepository = doctorRepository;
        this.doctorAuditTrailRepository = doctorAuditTrailRepository;
    }

    @Override
    public DoctorResponseResource rehire(@MLN String medicalLicenseNumber) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        onActiveDoctor(doctor);
        DoctorAuditTrail doctorAuditTrail = DoctorAuditTrail.of(doctor);
        doctorAuditTrailRepository.save(doctorAuditTrail);
        doctor.markAsRehired();
        Doctor rehiredDoctor = doctorRepository.save(doctor);
        return DoctorResponseMapper.mapToResource(rehiredDoctor)
                .add(
                        linkTo(
                                methodOn(DoctorController.class).rehire(medicalLicenseNumber)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber)
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber)
                        ).withRel("terminate_doctor_by_medical_license_number")
                );
    }

    private void onActiveDoctor(Doctor doctor) {
        if (doctor.getTerminatedAt() == null) {
            String message = "Doctor whose medical license number is %s is active.";
            throw new ImmutableDoctorStatusException(message);
        }
    }
}

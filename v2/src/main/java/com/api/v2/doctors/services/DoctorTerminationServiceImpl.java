package com.api.v2.doctors.services;

import com.api.v2.common.*;
import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorAuditTrail;
import com.api.v2.doctors.domain.DoctorAuditTrailRepository;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DoctorTerminationServiceImpl implements DoctorTerminationService {

    private final DoctorFinderUtil doctorFinderUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorAuditTrailRepository doctorAuditTrailRepository;

    public DoctorTerminationServiceImpl(DoctorFinderUtil doctorFinderUtil,
                                        DoctorRepository doctorRepository,
                                        DoctorAuditTrailRepository doctorAuditTrailRepository
    ) {
        this.doctorFinderUtil = doctorFinderUtil;
        this.doctorRepository = doctorRepository;
        this.doctorAuditTrailRepository = doctorAuditTrailRepository;
    }

    @Override
    public Response<DoctorResponseResource> terminate(@MLN String medicalLicenseNumber) {
        Response<Doctor> doctorResponse = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        Doctor doctor = doctorResponse.getData();
        if (isDoctorTerminated(doctor)) {
            return onTerminatedDoctor();
        }
        DoctorAuditTrail doctorAuditTrail = DoctorAuditTrail.of(doctor);
        doctorAuditTrailRepository.save(doctorAuditTrail);
        doctor.markAsTerminated();
        Doctor terminaredDoctor = doctorRepository.save(doctor);
        DoctorResponseResource responseResource = DoctorResponseMapper.mapToResource(terminaredDoctor)
                .add(
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber)
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).rehire(medicalLicenseNumber)
                        ).withRel("rehire_doctor_by_medical_license_number")
                );
        return SuccessfulResponse.success(responseResource);
    }

    private boolean isDoctorTerminated(Doctor doctor) {
        return doctor.getTerminatedAt() != null;
    }

    private Response<DoctorResponseResource> onTerminatedDoctor() {
        String errorType = "Immutable doctor";
        String errorMessage = "Doctor is already terminated.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

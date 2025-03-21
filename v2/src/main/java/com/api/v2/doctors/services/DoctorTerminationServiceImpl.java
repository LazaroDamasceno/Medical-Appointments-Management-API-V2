package com.api.v2.doctors.services;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorAuditTrail;
import com.api.v2.doctors.domain.DoctorAuditTrailRepository;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.exceptions.ImmutableDoctorStatusException;
import com.api.v2.doctors.utils.DoctorFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DoctorTerminationServiceImpl implements DoctorTerminationService {

    private final DoctorFinder doctorFinder;
    private final DoctorRepository doctorRepository;
    private final DoctorAuditTrailRepository doctorAuditTrailRepository;

    public DoctorTerminationServiceImpl(DoctorFinder doctorFinder,
                                        DoctorRepository doctorRepository,
                                        DoctorAuditTrailRepository doctorAuditTrailRepository
    ) {
        this.doctorFinder = doctorFinder;
        this.doctorRepository = doctorRepository;
        this.doctorAuditTrailRepository = doctorAuditTrailRepository;
    }

    @Override
    public ResponseEntity<ResourceResponse> terminate(String medicalLicenseNumber, String state) {
        Doctor doctor = doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber, state);
        onTerminatedDoctor(doctor);
        DoctorAuditTrail doctorAuditTrail = DoctorAuditTrail.of(doctor);
        doctorAuditTrailRepository.save(doctorAuditTrail);
        doctor.markAsTerminated();
        Doctor terminaredDoctor = doctorRepository.save(doctor);
        ResourceResponse responseResource = ResourceResponse
                .createEmpty()
                .add(
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber, state)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber, state)
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).rehire(medicalLicenseNumber, state)
                        ).withRel("rehire_doctor_by_medical_license_number")
                );
        return ResponseEntity.ok(responseResource);
    }

    private void onTerminatedDoctor(Doctor doctor) {
        if (doctor.getTerminatedAt() != null) {
            String message = "Doctor whose medical license number is %s is already terminated.";
            throw new ImmutableDoctorStatusException(message);
        }
    }
}

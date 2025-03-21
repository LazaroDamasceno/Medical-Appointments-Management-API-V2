package com.api.v2.doctors.services;

import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.utils.DoctorFinder;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DoctorRetrievalServiceImpl implements DoctorRetrievalService {

    private final DoctorRepository doctorRepository;
    private final DoctorFinder doctorFinder;

    public DoctorRetrievalServiceImpl(DoctorRepository doctorRepository,
                                      DoctorFinder doctorFinder
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorFinder = doctorFinder;
    }

    @Override
    public ResponseEntity<DoctorResponseResource> findByMedicalLicenseNumber(String medicalLicenseNumber, String state) {
        DoctorResponseResource responseResource = DoctorResponseMapper
                .mapToResource(doctorFinder.findByMedicalLicenseNumber(medicalLicenseNumber, state))
                .add(
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber, state)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber, state)
                        ).withRel("terminate_doctor_by_medical_license_number")
                );
        return ResponseEntity.ok(responseResource);
    }

    @Override
    public ResponseEntity<List<DoctorResponseResource>> findAll() {
        List<DoctorResponseResource> list = doctorRepository
                .findAll()
                .stream()
                .map(DoctorResponseMapper::mapToResource)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}

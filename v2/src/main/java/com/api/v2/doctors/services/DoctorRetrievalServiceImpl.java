package com.api.v2.doctors.services;

import com.api.v2.common.MLN;
import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.exposed.DoctorResponseDto;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DoctorRetrievalServiceImpl implements DoctorRetrievalService {

    private final DoctorRepository doctorRepository;
    private final DoctorFinderUtil doctorFinderUtil;

    public DoctorRetrievalServiceImpl(DoctorRepository doctorRepository,
                                      DoctorFinderUtil doctorFinderUtil
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorFinderUtil = doctorFinderUtil;
    }

    @Override
    public DoctorResponseDto findByMedicalLicenseNumber(@MLN String medicalLicenseNumber) {
        return DoctorResponseMapper
                .mapToResource(doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber))
                .add(
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber)
                        ).withRel("terminate_doctor_by_medical_license_number")
                );
    }

    @Override
    public List<DoctorResponseDto> findAll() {
        return doctorRepository
                .findAll()
                .stream()
                .map(DoctorResponseMapper::mapToResource)
                .toList();
    }
}

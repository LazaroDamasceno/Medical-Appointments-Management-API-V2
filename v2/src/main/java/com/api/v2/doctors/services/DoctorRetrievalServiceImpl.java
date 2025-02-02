package com.api.v2.doctors.services;

import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public DoctorResponseResource findByMedicalLicenseNumber(String medicalLicenseNumber) {
        return DoctorResponseMapper.mapToDto(doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber));
    }

    @Override
    public List<DoctorResponseResource> findAll() {
        return doctorRepository
                .findAll()
                .stream()
                .map(DoctorResponseMapper::mapToDto)
                .toList();
    }
}

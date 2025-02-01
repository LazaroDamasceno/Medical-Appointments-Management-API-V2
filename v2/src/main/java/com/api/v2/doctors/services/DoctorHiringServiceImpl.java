package com.api.v2.doctors.services;

import com.api.v2.doctors.domain.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.dto.DoctorResponseDto;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.people.domain.Person;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class DoctorHiringServiceImpl implements DoctorHiringService {

    private final DoctorRepository doctorRepository;
    private final PersonRegistrationService personRegistrationService;

    public DoctorHiringServiceImpl(DoctorRepository doctorRepository,
                                   PersonRegistrationService personRegistrationService
    ) {
        this.doctorRepository = doctorRepository;
        this.personRegistrationService = personRegistrationService;
    }

    @Override
    public DoctorResponseDto hire(@Valid DoctorHiringDto hiringDto) {
        Person savedPerson = personRegistrationService.register(hiringDto.personRegistrationDto());
        Doctor doctor = Doctor.create(savedPerson, hiringDto.medicalLicenseNumber());
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorResponseMapper.mapToDto(savedDoctor);
    }
}

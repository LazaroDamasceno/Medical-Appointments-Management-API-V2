package com.api.v2.doctors.services;

import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.exceptions.DuplicatedMedicalLicenseNumberException;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.people.domain.Person;
import com.api.v2.people.exceptions.DuplicatedEmailException;
import com.api.v2.people.exceptions.DuplicatedSsnException;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public EntityModel<DoctorResponseResource> hire(@Valid DoctorHiringDto hiringDto) {
        onDuplicatedSsn(hiringDto.personRegistrationDto().ssn());
        onDuplicatedEmail(hiringDto.personRegistrationDto().email());
        onDuplicatedMedicalLicenseNumber(hiringDto.medicalLicenseNumber());
        Person savedPerson = personRegistrationService.register(hiringDto.personRegistrationDto());
        Doctor doctor = Doctor.create(savedPerson, hiringDto.medicalLicenseNumber());
        Doctor savedDoctor = doctorRepository.save(doctor);
        DoctorResponseResource responseDto = DoctorResponseMapper.mapToDto(savedDoctor);
        return EntityModel
                .of(responseDto)
                .add(
                        linkTo(
                                methodOn(DoctorController.class).hire(hiringDto)
                        ).withSelfRel(),
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(hiringDto.medicalLicenseNumber())
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).terminate(hiringDto.medicalLicenseNumber())
                        ).withRel("terminate_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).findAll()
                        ).withRel("find_all_doctors")
                );
    }

    private void onDuplicatedSsn(String ssn) {
        boolean isSsnDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getSsn().equals(ssn));
        if (isSsnDuplicated) {
            throw new DuplicatedSsnException();
        }
    }

    private void onDuplicatedEmail(String email) {
        boolean isEmailDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getEmail().equals(email));
        if (isEmailDuplicated) {
            throw new DuplicatedEmailException();
        }
    }

    private void onDuplicatedMedicalLicenseNumber(String medicalLicenseNumber) {
        boolean isMedicalLicenseNumberDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber));
        if (isMedicalLicenseNumberDuplicated) {
            throw new DuplicatedMedicalLicenseNumberException();
        }
    }
}

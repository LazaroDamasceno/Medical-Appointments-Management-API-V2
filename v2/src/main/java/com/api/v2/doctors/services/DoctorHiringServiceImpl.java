package com.api.v2.doctors.services;

import com.api.v2.common.DuplicatedPersonalDataHandler;
import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.exceptions.DuplicatedMedicalLicenseNumberException;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DoctorHiringServiceImpl implements DoctorHiringService {

    private final DoctorRepository doctorRepository;
    private final PersonRegistrationService personRegistrationService;
    private final DuplicatedPersonalDataHandler duplicatedPersonalDataHandler;

    public DoctorHiringServiceImpl(DoctorRepository doctorRepository,
                                   PersonRegistrationService personRegistrationService,
                                   DuplicatedPersonalDataHandler duplicatedPersonalDataHandler
    ) {
        this.doctorRepository = doctorRepository;
        this.personRegistrationService = personRegistrationService;
        this.duplicatedPersonalDataHandler = duplicatedPersonalDataHandler;
    }

    @Override
    public ResponseEntity<DoctorResponseResource> hire(@Valid DoctorHiringDto hiringDto) {
        validateRegistration(
                hiringDto.personRegistrationDto().ssn(),
                hiringDto.personRegistrationDto().email(),
                hiringDto.medicalLicenseNumber()
        );
        onDuplicatedMedicalLicenseNumber(hiringDto.medicalLicenseNumber());
        Person savedPerson = personRegistrationService.register(hiringDto.personRegistrationDto());
        Doctor doctor = Doctor.of(savedPerson, hiringDto.medicalLicenseNumber());
        Doctor savedDoctor = doctorRepository.save(doctor);
        String medicalLicenseNumber = hiringDto.medicalLicenseNumber().licenseNumber();
        String medicalRegion = hiringDto.medicalLicenseNumber().medicalRegion().toString();
        DoctorResponseResource responseResource = DoctorResponseMapper
                .mapToResource(savedDoctor)
                .add(
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber, medicalRegion)
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber, medicalRegion)
                        ).withRel("terminate_doctor_by_medical_license_number")
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseResource);
    }

    private void validateRegistration(String ssn, String email, MedicalLicenseNumber medicalLicenseNumber) {
        duplicatedPersonalDataHandler.handleDuplicatedSsn(ssn);
        duplicatedPersonalDataHandler.handleDuplicatedEmail(email);
        onDuplicatedMedicalLicenseNumber(medicalLicenseNumber);
    }

    private void onDuplicatedMedicalLicenseNumber(MedicalLicenseNumber medicalLicenseNumber) {
        boolean isMedicalLicenseNumberDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber));
        if (isMedicalLicenseNumberDuplicated) {
            throw new DuplicatedMedicalLicenseNumberException();
        }
    }
}

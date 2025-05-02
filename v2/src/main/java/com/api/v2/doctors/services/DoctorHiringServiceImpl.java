package com.api.v2.doctors.services;

import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.requests.DoctorHiringDto;
import com.api.v2.doctors.dtos.MedicalLicenseNumber;
import com.api.v2.doctors.responses.DoctorResponseResource;
import com.api.v2.doctors.exceptions.DuplicatedMedicalLicenseNumberException;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.exceptions.DuplicatedEmailException;
import com.api.v2.people.exceptions.DuplicatedSsnException;
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

    public DoctorHiringServiceImpl(DoctorRepository doctorRepository,
                                   PersonRegistrationService personRegistrationService
    ) {
        this.doctorRepository = doctorRepository;
        this.personRegistrationService = personRegistrationService;
    }

    @Override
    public ResponseEntity<DoctorResponseResource> hire(@Valid DoctorHiringDto hiringDto) {
        validateRegistration(
                hiringDto.personRegistrationDto().ssn(),
                hiringDto.personRegistrationDto().email(),
                hiringDto.medicalLicenseNumber()
        );
        validateRegistration(
                hiringDto.personRegistrationDto().ssn(),
                hiringDto.personRegistrationDto().email(),
                hiringDto.medicalLicenseNumber()
        );
        Person savedPerson = personRegistrationService.register(hiringDto.personRegistrationDto());
        Doctor doctor = Doctor.of(savedPerson, hiringDto.medicalLicenseNumber());
        Doctor savedDoctor = doctorRepository.save(doctor);
        String medicalLicenseNumber = hiringDto.medicalLicenseNumber().licenseNumber();
        String state = hiringDto.medicalLicenseNumber().state().toString();
        DoctorResponseResource responseResource = savedDoctor
                .toResource()
                .add(
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(medicalLicenseNumber, state)
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).terminate(medicalLicenseNumber, state)
                        ).withRel("terminate_doctor_by_medical_license_number")
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseResource);
    }

    private void validateRegistration(String ssn, String email, MedicalLicenseNumber medicalLicenseNumber) {
        boolean isSsnDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(x -> x.getPerson().getSsn().equals(ssn));
        if (isSsnDuplicated) {
            throw new DuplicatedSsnException();
        }

        boolean isEmailDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(x -> x.getPerson().getEmail().equals(email));
        if (isEmailDuplicated) {
            throw new DuplicatedEmailException();
        }

        boolean isMedicalLicenseNumberDuplicated = doctorRepository
                .findAll()
                .stream()
                .anyMatch(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber));
        if (isMedicalLicenseNumberDuplicated) {
            throw new DuplicatedMedicalLicenseNumberException();
        }
    }

}

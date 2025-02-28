package com.api.v2.doctors.services;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.common.SuccessfulResponse;
import com.api.v2.doctors.controller.DoctorController;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.utils.DoctorResponseMapper;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
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
    public Response<DoctorResponseResource> hire(@Valid DoctorHiringDto hiringDto) {
        if (isSsnDuplicated(hiringDto.personRegistrationDto().ssn())) {
            return onDuplicatedSsn();
        }
        if (isEmailDuplicated(hiringDto.personRegistrationDto().ssn())) {
            return onDuplicatedEmail();
        }
        if (isMedicalLicenseNumber(hiringDto.medicalLicenseNumber())) {
            return onDuplicatedMedicalLicenseNumber();
        }
        Person savedPerson = personRegistrationService.register(hiringDto.personRegistrationDto());
        Doctor doctor = Doctor.of(savedPerson, hiringDto.medicalLicenseNumber());
        Doctor savedDoctor = doctorRepository.save(doctor);
        DoctorResponseResource response = DoctorResponseMapper
                .mapToResource(savedDoctor)
                .add(
                        linkTo(
                                methodOn(DoctorController.class).findByMedicalLicenseNumber(hiringDto.medicalLicenseNumber())
                        ).withRel("find_doctor_by_medical_license_number"),
                        linkTo(
                                methodOn(DoctorController.class).terminate(hiringDto.medicalLicenseNumber())
                        ).withRel("terminate_doctor_by_medical_license_number")
                );
        return SuccessfulResponse.success(Constants.CREATED_201, response);
    }

    private boolean isSsnDuplicated(String ssn) {
        return doctorRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getSsn().equals(ssn));
    }

    private boolean isEmailDuplicated(String email) {
        return doctorRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getEmail().equals(email));
    }

    private boolean isMedicalLicenseNumber(String medicalLicenseNumber) {
        return doctorRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getMedicalLicenseNumber().equals(medicalLicenseNumber));
    }

    private Response<DoctorResponseResource> onDuplicatedSsn() {
        String errorType = "Duplicated SSN";
        String errorMessage = "Given SSN is already in use.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<DoctorResponseResource> onDuplicatedEmail() {
        String errorType = "Duplicated email";
        String errorMessage = "Given SSN is already in use.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<DoctorResponseResource> onDuplicatedMedicalLicenseNumber() {
        String errorType = "Duplicated medical license number";
        String errorMessage = "Given medical license number is already in use.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

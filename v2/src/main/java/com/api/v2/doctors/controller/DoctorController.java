package com.api.v2.doctors.controller;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.dto.DoctorResponseDto;
import com.api.v2.doctors.services.*;
import com.api.v2.people.dtos.PersonModificationDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/doctors")
public class DoctorController {

    private final DoctorHiringService hiringService;
    private final DoctorModificationService modificationService;
    private final DoctorRehireService rehireService;
    private final DoctorRetrievalService retrievalService;
    private final DoctorTerminationService terminationService;

    public DoctorController(DoctorHiringService hiringService, DoctorModificationService modificationService, DoctorRehireService rehireService, DoctorRetrievalService retrievalService, DoctorTerminationService terminationService) {
        this.hiringService = hiringService;
        this.modificationService = modificationService;
        this.rehireService = rehireService;
        this.retrievalService = retrievalService;
        this.terminationService = terminationService;
    }


    @PostMapping
    public DoctorResponseDto hire(@RequestBody DoctorHiringDto hiringDto) {
        return hiringService.hire(hiringDto);
    }

    @PutMapping("{medicalLicenseNumber}")
    public void modify(
            @PathVariable String medicalLicenseNumber,
            @Valid @RequestBody PersonModificationDto modificationDto
    ) {
        modificationService.modify(medicalLicenseNumber, modificationDto);
    }

    @PatchMapping("{medicalLicenseNumber}/rehiring")
    public void rehire(@PathVariable String medicalLicenseNumber) {
        rehireService.rehire(medicalLicenseNumber);
    }

    @PatchMapping("{medicalLicenseNumber}/termination")
    public void terminate(@PathVariable String medicalLicenseNumber) {
        terminationService.terminate(medicalLicenseNumber);
    }

    @GetMapping("{medicalLicenseNumber}")
    public DoctorResponseDto findByMedicalLicenseNumber(@PathVariable String medicalLicenseNumber) {
        return retrievalService.findByMedicalLicenseNumber(medicalLicenseNumber);
    }

    @GetMapping
    public List<DoctorResponseDto> findAll() {
        return retrievalService.findAll();
    }
}

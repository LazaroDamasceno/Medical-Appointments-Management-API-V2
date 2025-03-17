package com.api.v2.doctors.controller;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.dto.MedicalLicenseNumber;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.services.DoctorHiringService;
import com.api.v2.doctors.services.DoctorRehireService;
import com.api.v2.doctors.services.DoctorRetrievalService;
import com.api.v2.doctors.services.DoctorTerminationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/doctors")
public class DoctorController {

    private final DoctorHiringService hiringService;
    private final DoctorRehireService rehireService;
    private final DoctorRetrievalService retrievalService;
    private final DoctorTerminationService terminationService;

    public DoctorController(DoctorHiringService hiringService,
                            DoctorRehireService rehireService,
                            DoctorRetrievalService retrievalService,
                            DoctorTerminationService terminationService
    ) {
        this.hiringService = hiringService;
        this.rehireService = rehireService;
        this.retrievalService = retrievalService;
        this.terminationService = terminationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DoctorResponseResource> hire(@RequestBody DoctorHiringDto hiringDto) {
        return hiringService.hire(hiringDto);
    }

    @PatchMapping("rehiring")
    public ResponseEntity<DoctorResponseResource> rehire(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber) {
        return rehireService.rehire(medicalLicenseNumber);
    }

    @PatchMapping("termination")
    public ResponseEntity<DoctorResponseResource> terminate(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber) {
        return terminationService.terminate(medicalLicenseNumber);
    }

    @GetMapping
    public ResponseEntity<DoctorResponseResource> findByMedicalLicenseNumber(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber) {
        return retrievalService.findByMedicalLicenseNumber(medicalLicenseNumber);
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseResource>> findAll() {
        return retrievalService.findAll();
    }
}

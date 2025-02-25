package com.api.v2.doctors.controller;

import com.api.v2.common.MLN;
import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.doctors.services.*;
import com.api.v2.people.dtos.PersonModificationDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public DoctorResponseResource hire(@RequestBody DoctorHiringDto hiringDto) {
        return hiringService.hire(hiringDto);
    }

    @PatchMapping("{medicalLicenseNumber}/rehiring")
    public DoctorResponseResource rehire(@PathVariable @MLN String medicalLicenseNumber) {
        return rehireService.rehire(medicalLicenseNumber);
    }

    @PatchMapping("{medicalLicenseNumber}/termination")
    public DoctorResponseResource terminate(@PathVariable @MLN String medicalLicenseNumber) {
        return terminationService.terminate(medicalLicenseNumber);
    }

    @GetMapping("{medicalLicenseNumber}")
    public DoctorResponseResource findByMedicalLicenseNumber(@PathVariable @MLN String medicalLicenseNumber) {
        return retrievalService.findByMedicalLicenseNumber(medicalLicenseNumber);
    }

    @GetMapping
    public List<DoctorResponseResource> findAll() {
        return retrievalService.findAll();
    }
}

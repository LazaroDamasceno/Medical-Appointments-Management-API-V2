package com.api.v2.doctors.controller;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.requests.DoctorHiringDto;
import com.api.v2.doctors.responses.DoctorResponseResource;
import com.api.v2.doctors.services.DoctorHiringService;
import com.api.v2.doctors.services.DoctorRehireService;
import com.api.v2.doctors.services.DoctorRetrievalService;
import com.api.v2.doctors.services.DoctorTerminationService;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Hire a new doctor")
    @PostMapping
    public ResponseEntity<DoctorResponseResource> hire(@RequestBody DoctorHiringDto hiringDto) {
        return hiringService.hire(hiringDto);
    }

    @Operation(summary = "Terminate a doctor")
    @PatchMapping("{medicalLicenseNumber}/{state}/rehiring")
    public ResponseEntity<ResourceResponse> rehire(@PathVariable String medicalLicenseNumber,
                                                   @PathVariable String state
    ) {
        return rehireService.rehire(medicalLicenseNumber, state);
    }

    @Operation(summary = "Cancel a doctor")
    @PatchMapping("{medicalLicenseNumber}/{state}/termination")
    public ResponseEntity<ResourceResponse> terminate(@PathVariable String medicalLicenseNumber,
                                                      @PathVariable String state
    ) {
        return terminationService.terminate(medicalLicenseNumber, state);
    }

    @Operation(summary = "Retrieve a doctor by its id")
    @GetMapping("{medicalLicenseNumber}/{state}")
    public ResponseEntity<DoctorResponseResource> findByMedicalLicenseNumber(@PathVariable String medicalLicenseNumber,
                                                                             @PathVariable String state
    ) {
        return retrievalService.findByMedicalLicenseNumber(medicalLicenseNumber, state);
    }

    @Operation(summary = "Retrieve all doctors")
    @GetMapping
    public ResponseEntity<List<DoctorResponseResource>> findAll() {
        return retrievalService.findAll();
    }
}

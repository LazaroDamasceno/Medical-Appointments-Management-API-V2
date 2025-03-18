package com.api.v2.medical_slots.controllers;

import com.api.v2.common.ResourceResponse;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.services.MedicalSlotManagementService;
import com.api.v2.medical_slots.services.MedicalSlotRegistrationService;
import com.api.v2.medical_slots.services.MedicalSlotRetrievalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/medical-slots")
public class MedicalSlotController {

    private final MedicalSlotRegistrationService registrationService;
    private final MedicalSlotManagementService managementService;
    private final MedicalSlotRetrievalService retrievalService;

    public MedicalSlotController(MedicalSlotRegistrationService registrationService,
                                 MedicalSlotManagementService managementService,
                                 MedicalSlotRetrievalService retrievalService
    ) {
        this.registrationService = registrationService;
        this.managementService = managementService;
        this.retrievalService = retrievalService;
    }

    @PostMapping
    public ResponseEntity<MedicalSlotResponseResource> register(@RequestBody @Valid MedicalSlotRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @PatchMapping("{medicalLicenseNumber}/{medicalRegion}/{slotId}/cancellation")
    public ResponseEntity<ResourceResponse> cancel(@PathVariable String medicalLicenseNumber,
                                                   @PathVariable String medicalRegion,
                                                   @PathVariable String slotId
    ) {
        return managementService.cancelById(medicalLicenseNumber, medicalRegion, slotId);
    }

    @PatchMapping("{medicalLicenseNumber}/{medicalRegion}/{slotId}/completion")
    public ResponseEntity<ResourceResponse> complete(@PathVariable String medicalLicenseNumber,
                                                     @PathVariable String medicalRegion,
                                                     @PathVariable String slotId
    ) {
        return managementService.completeById(medicalLicenseNumber, medicalRegion, slotId);
    }

    @GetMapping("{medicalLicenseNumber}/{medicalRegion}/{slotId}")
    public ResponseEntity<MedicalSlotResponseResource> findById(@PathVariable String medicalLicenseNumber,
                                                                @PathVariable String medicalRegion,
                                                                @PathVariable String slotId
    ) {
        return retrievalService.findById(medicalLicenseNumber, medicalRegion, slotId);
    }

    @GetMapping("{medicalLicenseNumber}/{medicalRegion}")
    public ResponseEntity<List<MedicalSlotResponseResource>> findAllByDoctor(@PathVariable String medicalLicenseNumber,
                                                                             @PathVariable String medicalRegion
    ) {
        return retrievalService.findAllByDoctor(medicalLicenseNumber, medicalRegion);
    }

    @GetMapping
    public ResponseEntity<List<MedicalSlotResponseResource>> findAll() {
        return retrievalService.findAll();
    }
}

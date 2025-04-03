package com.api.v2.medical_slots.controllers;

import com.api.v2.common.ResourceResponse;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.services.MedicalSlotManagementService;
import com.api.v2.medical_slots.services.MedicalSlotRegistrationService;
import com.api.v2.medical_slots.services.MedicalSlotRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Register a new medical slot")
    @PostMapping
    public ResponseEntity<MedicalSlotResponseResource> register(@RequestBody @Valid MedicalSlotRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @Operation(summary = "Cancel a medical slot")
    @PatchMapping("{medicalLicenseNumber}/{state}/{slotId}/cancellation")
    public ResponseEntity<ResourceResponse> cancel(@PathVariable String medicalLicenseNumber,
                                                   @PathVariable String state,
                                                   @PathVariable String slotId
    ) {
        return managementService.cancelById(medicalLicenseNumber, state, slotId);
    }

    @Operation(summary = "Complete a medical slot")
    @PatchMapping("{medicalLicenseNumber}/{state}/{slotId}/completion")
    public ResponseEntity<ResourceResponse> complete(@PathVariable String medicalLicenseNumber,
                                                     @PathVariable String state,
                                                     @PathVariable String slotId
    ) {
        return managementService.completeById(medicalLicenseNumber, state, slotId);
    }

    @Operation(summary = "Retrieve a medical slot by its id and doctor")
    @GetMapping("{medicalLicenseNumber}/{state}/{slotId}")
    public ResponseEntity<MedicalSlotResponseResource> findById(@PathVariable String medicalLicenseNumber,
                                                                @PathVariable String state,
                                                                @PathVariable String slotId
    ) {
        return retrievalService.findById(medicalLicenseNumber, state, slotId);
    }

    @Operation(summary = "Retrieve a medical slot by doctor")
    @GetMapping("{medicalLicenseNumber}/{state}")
    public ResponseEntity<List<MedicalSlotResponseResource>> findAllByDoctor(@PathVariable String medicalLicenseNumber,
                                                                             @PathVariable String state
    ) {
        return retrievalService.findAllByDoctor(medicalLicenseNumber, state);
    }

    @Operation(summary = "Retrieve all medical slots")
    @GetMapping
    public ResponseEntity<List<MedicalSlotResponseResource>> findAll() {
        return retrievalService.findAll();
    }
}

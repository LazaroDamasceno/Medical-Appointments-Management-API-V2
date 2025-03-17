package com.api.v2.medical_slots.controllers;

import com.api.v2.common.ResourceResponse;
import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.medical_slots.dto.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.services.MedicalSlotCancellationService;
import com.api.v2.medical_slots.services.MedicalSlotCompletionService;
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
    private final MedicalSlotCancellationService cancellationService;
    private final MedicalSlotRetrievalService retrievalService;
    private final MedicalSlotCompletionService completionService;

    public MedicalSlotController(MedicalSlotRegistrationService registrationService,
                                 MedicalSlotCancellationService cancellationService,
                                 MedicalSlotRetrievalService retrievalService, MedicalSlotCompletionService completionService
    ) {
        this.registrationService = registrationService;
        this.cancellationService = cancellationService;
        this.retrievalService = retrievalService;
        this.completionService = completionService;
    }

    @PostMapping
    public ResponseEntity<MedicalSlotResponseResource> register(@RequestBody @Valid MedicalSlotRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @PatchMapping("{slotId}/cancellation")
    public ResponseEntity<ResourceResponse> cancel(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber, @PathVariable String slotId) {
        return cancellationService.cancelById(medicalLicenseNumber, slotId);
    }

    @PatchMapping("{slotId}/completion")
    public ResponseEntity<ResourceResponse> complete(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber, @PathVariable String slotId) {
        return completionService.completeById(medicalLicenseNumber, slotId);
    }

    @GetMapping("{medicalLicenseNumber}/{slotId}")
    public ResponseEntity<MedicalSlotResponseResource> findById(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber, @PathVariable String slotId) {
        return retrievalService.findById(medicalLicenseNumber, slotId);
    }

    @GetMapping("{medicalLicenseNumber}")
    public ResponseEntity<List<MedicalSlotResponseResource>> findAllByDoctor(@RequestBody @Valid MedicalLicenseNumber medicalLicenseNumber) {
        return retrievalService.findAllByDoctor(medicalLicenseNumber);
    }

    @GetMapping
    public ResponseEntity<List<MedicalSlotResponseResource>> findAll() {
        return retrievalService.findAll();
    }
}

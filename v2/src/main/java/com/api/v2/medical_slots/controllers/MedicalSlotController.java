package com.api.v2.medical_slots.controllers;

import com.api.v2.medical_slots.resources.MedicalSlotResponseResource;
import com.api.v2.medical_slots.services.MedicalSlotCancellationService;
import com.api.v2.medical_slots.services.MedicalSlotRegistrationService;
import com.api.v2.medical_slots.services.MedicalSlotRetrievalService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v2/medical-slots")
public class MedicalSlotController {

    private final MedicalSlotRegistrationService registrationService;
    private final MedicalSlotCancellationService cancellationService;
    private final MedicalSlotRetrievalService retrievalService;

    public MedicalSlotController(MedicalSlotRegistrationService registrationService,
                                 MedicalSlotCancellationService cancellationService,
                                 MedicalSlotRetrievalService retrievalService
    ) {
        this.registrationService = registrationService;
        this.cancellationService = cancellationService;
        this.retrievalService = retrievalService;
    }

    @PostMapping("medical-license-number/{medicalLicenseNumber}/available-at/{availableAt}")
    public MedicalSlotResponseResource register(@PathVariable String medicalLicenseNumber,
                                                @PathVariable LocalDateTime availableAt
    ) {
        return registrationService.register(medicalLicenseNumber, availableAt);
    }

    @PatchMapping("{id}/cancellation")
    public MedicalSlotResponseResource cancel(@PathVariable String id) {
        return cancellationService.cancel(id);
    }

    @GetMapping("{id}")
    public MedicalSlotResponseResource findById(@PathVariable String id) {
        return retrievalService.findById(id);
    }

    @GetMapping("{medicalLicenseNumber}")
    public List<MedicalSlotResponseResource> findByDoctor(@PathVariable String medicalLicenseNumber) {
        return retrievalService.findByDoctor(medicalLicenseNumber);
    }

    @GetMapping
    public List<MedicalSlotResponseResource> findAll() {
        return retrievalService.findAll();
    }
}

package com.api.v2.medical_slots.controllers;

import com.api.v2.common.MLN;
import com.api.v2.medical_slots.dtos.MedicalSlotRegistrationDto;
import com.api.v2.medical_slots.dtos.MedicalSlotResponseDto;
import com.api.v2.medical_slots.services.MedicalSlotCancellationService;
import com.api.v2.medical_slots.services.MedicalSlotCompletionService;
import com.api.v2.medical_slots.services.MedicalSlotRegistrationService;
import com.api.v2.medical_slots.services.MedicalSlotRetrievalService;
import jakarta.validation.Valid;
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
    public MedicalSlotResponseDto register(@RequestBody @Valid MedicalSlotRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @PatchMapping("{medicalLicenseNumber}/{slotId}/cancellation")
    public MedicalSlotResponseDto cancel(@PathVariable @MLN String medicalLicenseNumber, @PathVariable String slotId) {
        return cancellationService.cancelById(medicalLicenseNumber, slotId);
    }

    @PatchMapping("{medicalLicenseNumber}/{slotId}/completion")
    public MedicalSlotResponseDto complete(@PathVariable @MLN String medicalLicenseNumber, @PathVariable String slotId) {
        return completionService.completeById(medicalLicenseNumber, slotId);
    }

    @GetMapping("{medicalLicenseNumber}/{slotId}")
    public MedicalSlotResponseDto findById(@PathVariable @MLN String medicalLicenseNumber, @PathVariable String slotId) {
        return retrievalService.findById(medicalLicenseNumber, slotId);
    }

    @GetMapping("{medicalLicenseNumber}")
    public List<MedicalSlotResponseDto> findAllByDoctor(@PathVariable @MLN String medicalLicenseNumber) {
        return retrievalService.findAllByDoctor(medicalLicenseNumber);
    }

    @GetMapping
    public List<MedicalSlotResponseDto> findAll() {
        return retrievalService.findAll();
    }
}

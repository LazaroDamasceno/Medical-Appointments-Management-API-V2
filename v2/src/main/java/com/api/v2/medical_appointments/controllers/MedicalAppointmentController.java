package com.api.v2.medical_appointments.controllers;

import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.services.MedicalAppointmentBookingService;
import com.api.v2.medical_appointments.services.MedicalAppointmentCancellationService;
import com.api.v2.medical_appointments.services.MedicalAppointmentRetrievalService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v2/medical-appointments")
public class MedicalAppointmentController {

    private final MedicalAppointmentBookingService bookingService;
    private final MedicalAppointmentCancellationService cancellationService;
    private final MedicalAppointmentRetrievalService retrievalService;

    public MedicalAppointmentController(MedicalAppointmentBookingService bookingService,
                                        MedicalAppointmentCancellationService cancellationService,
                                        MedicalAppointmentRetrievalService retrievalService
    ) {
        this.bookingService = bookingService;
        this.cancellationService = cancellationService;
        this.retrievalService = retrievalService;
    }

    @PostMapping("{customerId}/{availableAt}/{medicalSlotId}")
    public MedicalAppointmentResponseResource book(@PathVariable String customerId,
                                                   @RequestBody LocalDateTime availableAt,
                                                   @PathVariable String medicalSlotId) {
        return bookingService.book(customerId, availableAt, medicalSlotId);
    }

    @PatchMapping("{customerId}/{medicalAppointmentId}")
    public MedicalAppointmentResponseResource cancel(@PathVariable String customerId,
                                                     @PathVariable String medicalAppointmentId) {
        return cancellationService.cancel(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}/{medicalAppointmentId}")
    public MedicalAppointmentResponseResource findById(@PathVariable String customerId,
                                                       @PathVariable String medicalAppointmentId
    ) {
        return retrievalService.findById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}")
    public List<MedicalAppointmentResponseResource> findAllByCustomer(@PathVariable String customerId) {
        return retrievalService.findAllByCustomer(customerId);
    }

    @GetMapping
    public List<MedicalAppointmentResponseResource> findAll() {
        return retrievalService.findAll();
    }
}

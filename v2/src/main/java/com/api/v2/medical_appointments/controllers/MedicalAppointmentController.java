package com.api.v2.medical_appointments.controllers;

import com.api.v2.common.Id;
import com.api.v2.common.ResourceResponse;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.services.MedicalAppointmentBookingService;
import com.api.v2.medical_appointments.services.MedicalAppointmentCancellationService;
import com.api.v2.medical_appointments.services.MedicalAppointmentRetrievalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<MedicalAppointmentResponseResource> book(@RequestBody @Valid MedicalAppointmentBookingDto bookingDto) {
        return bookingService.book(bookingDto);
    }

    @PatchMapping("{customerId}/{medicalAppointmentId}/cancellation")
    public ResponseEntity<ResourceResponse> cancel(@PathVariable @Id String customerId,
                                                   @PathVariable @Id String medicalAppointmentId) {
        return cancellationService.cancelById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}/{medicalAppointmentId}")
    public ResponseEntity<MedicalAppointmentResponseResource> findById(@PathVariable @Id String customerId,
                                                       @PathVariable @Id String medicalAppointmentId
    ) {
        return retrievalService.findById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}")
    public ResponseEntity<List<MedicalAppointmentResponseResource>> findAllByCustomer(@PathVariable @Id String customerId) {
        return retrievalService.findAllByCustomer(customerId);
    }

    @GetMapping
    public ResponseEntity<List<MedicalAppointmentResponseResource>> findAll() {
        return retrievalService.findAll();
    }
}

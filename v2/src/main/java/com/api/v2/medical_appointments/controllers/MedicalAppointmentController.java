package com.api.v2.medical_appointments.controllers;

import com.api.v2.common.ResourceResponse;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.resources.MedicalAppointmentResponseResource;
import com.api.v2.medical_appointments.services.MedicalAppointmentBookingService;
import com.api.v2.medical_appointments.services.MedicalAppointmentRetrievalService;
import com.api.v2.medical_appointments.services.exposed.MedicalAppointmentManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/medical-appointments")
public class MedicalAppointmentController {

    private final MedicalAppointmentBookingService bookingService;
    private final MedicalAppointmentManagementService managementService;
    private final MedicalAppointmentRetrievalService retrievalService;

    public MedicalAppointmentController(MedicalAppointmentBookingService bookingService,
                                        MedicalAppointmentManagementService managementService,
                                        MedicalAppointmentRetrievalService retrievalService
    ) {
        this.bookingService = bookingService;
        this.managementService = managementService;
        this.retrievalService = retrievalService;
    }

    @PostMapping("private-insurance")
    public ResponseEntity<MedicalAppointmentResponseResource> privateInsurance(@RequestBody @Valid MedicalAppointmentBookingDto bookingDto) {
        return bookingService.privateInsurance(bookingDto);
    }

    @PostMapping("public-insurance")
    public ResponseEntity<MedicalAppointmentResponseResource> publicInsurance(@RequestBody @Valid MedicalAppointmentBookingDto bookingDto) {
        return bookingService.publicInsurance(bookingDto);
    }

    @PostMapping("paid-by-patient")
    public ResponseEntity<MedicalAppointmentResponseResource> paidByPatient(@RequestBody @Valid MedicalAppointmentBookingDto bookingDto) {
        return bookingService.paidByPatient(bookingDto);
    }

    @PatchMapping("{customerId}/{medicalAppointmentId}/cancellation")
    public ResponseEntity<ResourceResponse> cancel(@PathVariable String customerId,
                                                   @PathVariable String medicalAppointmentId) {
        return managementService.cancelById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}/{medicalAppointmentId}")
    public ResponseEntity<MedicalAppointmentResponseResource> findById(@PathVariable String customerId,
                                                       @PathVariable String medicalAppointmentId
    ) {
        return retrievalService.findById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}")
    public ResponseEntity<List<MedicalAppointmentResponseResource>> findAllByCustomer(@PathVariable String customerId) {
        return retrievalService.findAllByCustomer(customerId);
    }

    @GetMapping
    public ResponseEntity<List<MedicalAppointmentResponseResource>> findAll() {
        return retrievalService.findAll();
    }

}

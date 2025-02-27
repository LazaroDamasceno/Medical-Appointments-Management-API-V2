package com.api.v2.medical_appointments.controllers;

import com.api.v2.common.Id;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v2.medical_appointments.dtos.MedicalAppointmentResponseDto;
import com.api.v2.medical_appointments.services.MedicalAppointmentBookingService;
import com.api.v2.medical_appointments.services.MedicalAppointmentCancellationService;
import com.api.v2.medical_appointments.services.MedicalAppointmentRetrievalService;
import jakarta.validation.Valid;
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
    public MedicalAppointmentResponseDto book(@RequestBody @Valid MedicalAppointmentBookingDto bookingDto) {
        return bookingService.book(bookingDto);
    }

    @PatchMapping("{customerId}/{medicalAppointmentId}")
    public MedicalAppointmentResponseDto cancel(@PathVariable @Id String customerId,
                                                @PathVariable @Id String medicalAppointmentId) {
        return cancellationService.cancelById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}/{medicalAppointmentId}")
    public MedicalAppointmentResponseDto findById(@PathVariable @Id String customerId,
                                                  @PathVariable @Id String medicalAppointmentId
    ) {
        return retrievalService.findById(customerId, medicalAppointmentId);
    }

    @GetMapping("{customerId}")
    public List<MedicalAppointmentResponseDto> findAllByCustomer(@PathVariable @Id String customerId) {
        return retrievalService.findAllByCustomer(customerId);
    }

    @GetMapping
    public List<MedicalAppointmentResponseDto> findAll() {
        return retrievalService.findAll();
    }
}

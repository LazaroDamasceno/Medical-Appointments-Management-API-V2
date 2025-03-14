package com.api.v2.customers.controller;

import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.services.CustomerRegistrationService;
import com.api.v2.customers.services.CustomerRetrievalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/customers")
public class CustomerController {

    private final CustomerRegistrationService registrationService;
    private final CustomerRetrievalService retrievalService;

    public CustomerController(CustomerRegistrationService registrationService,
                              CustomerRetrievalService retrievalService
    ) {
        this.registrationService = registrationService;
        this.retrievalService = retrievalService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> register(@Valid @RequestBody CustomerRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable String id) {
        return retrievalService.findById(id);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto> >findAll() {
        return retrievalService.findAll();
    }
}

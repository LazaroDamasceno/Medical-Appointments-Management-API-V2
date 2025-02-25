package com.api.v2.customers.controller;

import com.api.v2.common.Id;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.services.CustomerRegistrationService;
import com.api.v2.customers.services.CustomerRetrievalService;
import jakarta.validation.Valid;
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
    public CustomerResponseDto register(@Valid @RequestBody CustomerRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @GetMapping("{id}")
    public CustomerResponseDto findById(@PathVariable @Id String id) {
        return retrievalService.findById(id);
    }

    @GetMapping
    public List<CustomerResponseDto> findAll() {
        return retrievalService.findAll();
    }
}

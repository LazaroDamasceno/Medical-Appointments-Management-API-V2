package com.api.v2.customers.controller;

import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.CustomerResponseDto;
import com.api.v2.customers.services.CustomerModificationService;
import com.api.v2.customers.services.CustomerRegistrationService;
import com.api.v2.people.dtos.PersonModificationDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/")
public class CustomerController {

    private final CustomerRegistrationService registrationService;
    private final CustomerModificationService modificationService;

    public CustomerController(CustomerRegistrationService registrationService,
                              CustomerModificationService modificationService
    ) {
        this.registrationService = registrationService;
        this.modificationService = modificationService;
    }

    @PostMapping
    public CustomerResponseDto register(@Valid @RequestBody CustomerRegistrationDto registrationDto) {
        return registrationService.register(registrationDto);
    }

    @PatchMapping("{id}")
    public void modify(@PathVariable String id, @Valid @RequestBody PersonModificationDto modificationDto) {
        modificationService.modify(id, modificationDto);
    }
}

package com.api.v2.customers.services;

import com.api.v2.common.DuplicatedPersonalDataHandler;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PersonRegistrationService personRegistrationService;
    private final DuplicatedPersonalDataHandler duplicatedPersonalDataHandler;

    public CustomerRegistrationServiceImpl(CustomerRepository customerRepository,
                                           PersonRegistrationService personRegistrationService,
                                           DuplicatedPersonalDataHandler duplicatedPersonalDataHandler
    ) {
        this.customerRepository = customerRepository;
        this.personRegistrationService = personRegistrationService;
        this.duplicatedPersonalDataHandler = duplicatedPersonalDataHandler;
    }

    @Override
    public ResponseEntity<CustomerResponseDto> register(@Valid CustomerRegistrationDto registrationDto) {
        duplicatedPersonalDataHandler.handleDuplicatedSsn(registrationDto.personRegistrationDto().ssn());
        duplicatedPersonalDataHandler.handleDuplicatedEmail(registrationDto.personRegistrationDto().email());
        Person savedPerson = personRegistrationService.register(registrationDto.personRegistrationDto());
        Customer customer = Customer.of(registrationDto.address(), savedPerson);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerResponseDto responseDto = CustomerResponseMapper.mapToDto(savedCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}

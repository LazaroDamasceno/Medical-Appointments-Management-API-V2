package com.api.v3.customers.services;

import com.api.v3.customers.domain.Customer;
import com.api.v3.customers.domain.CustomerRepository;
import com.api.v3.customers.dtos.CustomerRegistrationDto;
import com.api.v3.customers.dtos.CustomerResponseDto;
import com.api.v3.customers.utils.CustomerResponseMapper;
import com.api.v3.people.domain.Person;
import com.api.v3.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PersonRegistrationService personRegistrationService;

    public CustomerRegistrationServiceImpl(CustomerRepository customerRepository,
                                           PersonRegistrationService personRegistrationService
    ) {
        this.customerRepository = customerRepository;
        this.personRegistrationService = personRegistrationService;
    }

    @Override
    public CustomerResponseDto register(@Valid CustomerRegistrationDto registrationDto) {
        Person savedPerson = personRegistrationService.register(registrationDto.personRegistrationDto());
        Customer customer = Customer.create(registrationDto.address(), savedPerson);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponseMapper.map(savedCustomer);
    }
}

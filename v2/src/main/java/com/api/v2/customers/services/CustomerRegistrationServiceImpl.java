package com.api.v2.customers.services;

import com.api.v2.customers.domain.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.people.domain.Person;
import com.api.v2.people.exceptions.DuplicatedEmailException;
import com.api.v2.people.exceptions.DuplicatedSsnException;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
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
        onDuplicatedSsn(registrationDto.personRegistrationDto().ssn());
        onDuplicatedEmail(registrationDto.personRegistrationDto().email());
        Person savedPerson = personRegistrationService.register(registrationDto.personRegistrationDto());
        Customer customer = Customer.create(registrationDto.address(), savedPerson);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponseMapper.mapToDto(savedCustomer);
    }

    private void onDuplicatedSsn(String ssn) {
        boolean isSsnDuplicated = customerRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getSsn().equals(ssn));
        if (isSsnDuplicated) {
            throw new DuplicatedSsnException();
        }
    }

    private void onDuplicatedEmail(String email) {
        boolean isEmailDuplicated = customerRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getEmail().equals(email));
        if (isEmailDuplicated) {
            throw new DuplicatedEmailException();
        }
    }
}

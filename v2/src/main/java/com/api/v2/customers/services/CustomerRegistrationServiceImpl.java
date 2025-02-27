package com.api.v2.customers.services;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.exceptions.DuplicatedEmailException;
import com.api.v2.people.exceptions.DuplicatedSsnException;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

    private CustomerRepository customerRepository;
    private PersonRegistrationService personRegistrationService;

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
        Customer customer = Customer.of(registrationDto.address(), savedPerson);
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

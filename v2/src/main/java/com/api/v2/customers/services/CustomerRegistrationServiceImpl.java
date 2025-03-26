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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CustomerResponseDto> register(@Valid CustomerRegistrationDto registrationDto) {
        validateRegistration(
                registrationDto.personRegistrationDto().ssn(),
                registrationDto.personRegistrationDto().email()
        );
        Person savedPerson = personRegistrationService.register(registrationDto.personRegistrationDto());
        Customer customer = Customer.of(registrationDto.address(), savedPerson);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerResponseDto responseDto = CustomerResponseMapper.toDto(savedCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    private void validateRegistration(String ssn, String email) {
        boolean isSsnDuplicated = customerRepository
                .findAll()
                .stream()
                .anyMatch(x -> x.getPerson().getSsn().equals(ssn));
        if (isSsnDuplicated) {
            throw new DuplicatedSsnException();
        }

        boolean isEmailDuplicated = customerRepository
                .findAll()
                .stream()
                .anyMatch(x -> x.getPerson().getEmail().equals(email));
        if (isEmailDuplicated) {
            throw new DuplicatedEmailException();
        }
    }
}

package com.api.v2.customers.services;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.common.SuccessfulResponse;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.dtos.CustomerRegistrationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.people.domain.exposed.Person;
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
    public Response<CustomerResponseDto> register(@Valid CustomerRegistrationDto registrationDto) {
        if (isSsnDuplicated(registrationDto.personRegistrationDto().ssn())) {
            return onDuplicatedSsn();
        }
        if (isEmailDuplicated(registrationDto.personRegistrationDto().ssn())) {
            return onDuplicatedEmail();
        }
        Person savedPerson = personRegistrationService.register(registrationDto.personRegistrationDto());
        Customer customer = Customer.of(registrationDto.address(), savedPerson);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerResponseDto responseDto = CustomerResponseMapper.mapToDto(savedCustomer);
        return SuccessfulResponse.success(Constants.CREATED_201, responseDto);
    }

    private boolean isSsnDuplicated(String ssn) {
        return customerRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getSsn().equals(ssn));
    }

    private boolean isEmailDuplicated(String email) {
        return customerRepository
                .findAll()
                .stream()
                .anyMatch(c -> c.getPerson().getEmail().equals(email));
    }

    private Response<CustomerResponseDto> onDuplicatedSsn() {
        String errorType = "Duplicated SSN";
        String errorMessage = "Given SSN is already in use.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }

    private Response<CustomerResponseDto> onDuplicatedEmail() {
        String errorType = "Duplicated email";
        String errorMessage = "Given SSN is already in use.";
        return ErrorResponse.error(Constants.CONFLICT_409, errorType, errorMessage);
    }
}

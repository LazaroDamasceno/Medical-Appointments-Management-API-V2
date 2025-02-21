package com.api.v2.customers.services;

import com.api.v2.common.Id;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.dtos.CustomerModificationDto;
import com.api.v2.customers.dtos.exposed.CustomerResponseDto;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.customers.utils.CustomerResponseMapper;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.services.interfaces.PersonModificationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerModificationServiceImpl implements CustomerModificationService {

    private final CustomerRepository customerRepository;
    private final CustomerFinderUtil customerFinderUtil;
    private final PersonModificationService personModificationService;

    public CustomerModificationServiceImpl(CustomerRepository customerRepository,
                                           CustomerFinderUtil customerFinderUtil,
                                           PersonModificationService personModificationService
    ) {
        this.customerRepository = customerRepository;
        this.customerFinderUtil = customerFinderUtil;
        this.personModificationService = personModificationService;
    }

    @Override
    public CustomerResponseDto modify(@Id String id, @Valid CustomerModificationDto modificationDto) {
        Customer customer = customerFinderUtil.findById(id);
        Person modifiedPerson = personModificationService.modify(customer.getPerson(), modificationDto.personModificationDto());
        customer.setPerson(modifiedPerson);
        customer.setAddress(modificationDto.address());
        Customer modifiedCustomer = customerRepository.save(customer);
        return CustomerResponseMapper.mapToDto(modifiedCustomer);
    }
}

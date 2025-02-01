package com.api.v2.customers.services;

import com.api.v2.customers.domain.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.utils.CustomerFinderUtil;
import com.api.v2.people.domain.Person;
import com.api.v2.people.dtos.PersonModificationDto;
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
    public void modify(String id, @Valid PersonModificationDto modificationDto) {
        Customer customer = customerFinderUtil.findById(id);
        Person modifiedPerson = personModificationService.modify(customer.getPerson(), modificationDto);
        customer.setPerson(modifiedPerson);
        customerRepository.save(customer);
    }
}

package com.api.v2.customers.utils;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.exceptions.NonExistentCustomerException;
import org.springframework.stereotype.Component;

@Component
public class CustomerFinder {

    private final CustomerRepository customerRepository;

    public CustomerFinder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findById(String id) {
        return customerRepository
                .findAll()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NonExistentCustomerException(id));
    }
}

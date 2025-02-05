package com.api.v2.customers.utils;

import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import com.api.v2.customers.exceptions.NonExistentCustomerException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerFinderUtil {

    private final CustomerRepository customerRepository;

    public CustomerFinderUtil(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findById(String id) {
        return customerRepository
                .findAll()
                .stream()
                .filter(c -> c.getPerson().getId().equals(new ObjectId(id)))
                .findFirst()
                .orElseThrow(() -> new NonExistentCustomerException(id));
    }
}

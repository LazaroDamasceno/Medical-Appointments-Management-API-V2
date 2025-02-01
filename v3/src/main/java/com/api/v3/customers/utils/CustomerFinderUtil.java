package com.api.v3.customers.utils;

import com.api.v3.customers.domain.Customer;
import com.api.v3.customers.domain.CustomerRepository;
import com.api.v3.customers.exceptions.NonExistentCustomerException;
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
        Optional<Customer> optionalCustomer = customerRepository
                .findAll()
                .stream()
                .filter(c -> c.getPerson().getId().equals(new ObjectId(id)))
                .findFirst();
        if (optionalCustomer.isEmpty()) {
            throw new NonExistentCustomerException(id);
        }
        return optionalCustomer.get();
    }
}

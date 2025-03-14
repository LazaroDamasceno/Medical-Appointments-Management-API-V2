package com.api.v2.customers.domain;

import com.api.v2.customers.domain.exposed.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}

package com.api.v2.customers.utils;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.customers.domain.CustomerRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerFinderUtil {

    private final CustomerRepository customerRepository;

    public CustomerFinderUtil(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Response<Customer> findById(String id) {
        Optional<Customer> optional = customerRepository
                .findAll()
                .stream()
                .filter(c -> c.getId().equals(new ObjectId(id)))
                .findFirst();
        if (optional.isEmpty()) {
            String errorType = "Resource not found";
            String errorMessage = "Customer whose id is %s was not found".formatted(id);
            return ErrorResponse.error(Constants.NOT_FOUND_404, errorType, errorMessage);
        }
        return Response.of(optional.get());
    }
}

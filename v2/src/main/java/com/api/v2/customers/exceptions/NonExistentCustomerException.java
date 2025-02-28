package com.api.v2.customers.exceptions;

public class NonExistentCustomerException extends RuntimeException {
    public NonExistentCustomerException(String id) {
        super("Customer whose id is %s was not found.".formatted(id));
    }
}

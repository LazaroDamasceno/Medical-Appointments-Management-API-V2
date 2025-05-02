package com.api.v2.payments.exceptions;

public class IllegalChargingException extends RuntimeException {
    public IllegalChargingException(String id) {
        super("Medical appointment whose id is %s is under the public health national program. It cannot be charged.".formatted(id));
    }
}

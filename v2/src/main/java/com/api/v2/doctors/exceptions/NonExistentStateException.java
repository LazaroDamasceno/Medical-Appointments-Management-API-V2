package com.api.v2.doctors.exceptions;

public class NonExistentStateException extends RuntimeException {
    public NonExistentStateException() {
        super("Given state does not exists.");
    }
}

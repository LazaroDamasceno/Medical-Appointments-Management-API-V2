package com.api.v2.people.exceptions;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException() {
        super("The given email is already in use.");
    }
}

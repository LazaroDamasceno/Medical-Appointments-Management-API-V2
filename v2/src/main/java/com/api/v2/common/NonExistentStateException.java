package com.api.v2.common;

public class NonExistentStateException extends RuntimeException {
    public NonExistentStateException() {
        super("Given state does not exists.");
    }
}

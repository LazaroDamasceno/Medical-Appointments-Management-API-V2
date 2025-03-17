package com.api.v2.doctors.exceptions;

public class NonExistentAmericanRegionException extends RuntimeException {
    public NonExistentAmericanRegionException() {
        super("Given region is not a valid option.");
    }
}

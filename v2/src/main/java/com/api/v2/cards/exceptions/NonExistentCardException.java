package com.api.v2.cards.exceptions;

public class NonExistentCardException extends RuntimeException {
    public NonExistentCardException(String id) {
        super("Card whose id is %s was not found.".formatted(id));
    }
}

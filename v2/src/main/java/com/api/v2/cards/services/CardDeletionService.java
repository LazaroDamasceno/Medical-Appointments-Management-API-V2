package com.api.v2.cards.services;

import org.springframework.http.ResponseEntity;

public interface CardDeletionService {
    ResponseEntity<Void> deleteById(String id);
}

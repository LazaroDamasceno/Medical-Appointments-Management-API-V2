package com.api.v2.cards.services;

import com.api.v2.cards.dtos.exposed.CardResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardRetrievalService {
    ResponseEntity<List<CardResponseDto>> findAll();
    ResponseEntity<CardResponseDto> findById(String id);
}

package com.api.v2.cards.services;

import com.api.v2.cards.dtos.CardResponseDto;

import java.util.List;

public interface CardRetrievalService {
    List<CardResponseDto> findAll();
    CardResponseDto findById(String id);
}

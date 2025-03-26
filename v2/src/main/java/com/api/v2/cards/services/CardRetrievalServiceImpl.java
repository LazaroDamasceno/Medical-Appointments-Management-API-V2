package com.api.v2.cards.services;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.dtos.exposed.CardResponseDto;
import com.api.v2.cards.utils.CardFinder;
import com.api.v2.cards.utils.CardResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardRetrievalServiceImpl implements CardRetrievalService {

    private final CardRepository cardRepository;
    private final CardFinder cardFinder;

    public CardRetrievalServiceImpl(CardRepository cardRepository,
                                    CardFinder cardFinder
    ) {
        this.cardRepository = cardRepository;
        this.cardFinder = cardFinder;
    }

    @Override
    public ResponseEntity<List<CardResponseDto>> findAll() {
        List<CardResponseDto> list = cardRepository
                .findAll()
                .stream()
                .map(CardResponseMapper::toDto)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<CardResponseDto> findById(String id) {
        Card card = cardFinder.findById(id);
        CardResponseDto responseDto = CardResponseMapper.toDto(card);
        return ResponseEntity.ok(responseDto);
    }
}

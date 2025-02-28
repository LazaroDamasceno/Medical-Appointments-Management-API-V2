package com.api.v2.cards.services;

import com.api.v2.cards.domain.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.dtos.CardResponseDto;
import com.api.v2.cards.util.CardFinderUtil;
import com.api.v2.cards.util.CardResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardRetrievalServiceImpl implements CardRetrievalService {

    private final CardRepository cardRepository;
    private final CardFinderUtil cardFinderUtil;

    public CardRetrievalServiceImpl(CardRepository cardRepository,
                                    CardFinderUtil cardFinderUtil
    ) {
        this.cardRepository = cardRepository;
        this.cardFinderUtil = cardFinderUtil;
    }

    @Override
    public ResponseEntity<List<CardResponseDto>> findAll() {
        List<CardResponseDto> list = cardRepository
                .findAll()
                .stream()
                .map(CardResponseMapper::map)
                .toList();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<CardResponseDto> findById(String id) {
        Card card = cardFinderUtil.findById(id);
        CardResponseDto responseDto = CardResponseMapper.map(card);
        return ResponseEntity.ok(responseDto);
    }
}

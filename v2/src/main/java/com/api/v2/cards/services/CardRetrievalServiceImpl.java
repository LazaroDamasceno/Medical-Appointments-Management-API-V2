package com.api.v2.cards.services;

import com.api.v2.cards.domain.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.dtos.CardResponseDto;
import com.api.v2.cards.util.CardFinderUtil;
import com.api.v2.cards.util.CardResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardRetrievalServiceImpl implements CardRetrievalService {

    private CardRepository cardRepository;
    private CardFinderUtil cardFinderUtil;

    public CardRetrievalServiceImpl(CardRepository cardRepository,
                                    CardFinderUtil cardFinderUtil
    ) {
        this.cardRepository = cardRepository;
        this.cardFinderUtil = cardFinderUtil;
    }

    @Override
    public List<CardResponseDto> findAll() {
        return cardRepository
                .findAll()
                .stream()
                .map(CardResponseMapper::map)
                .toList();
    }

    @Override
    public CardResponseDto findById(String id) {
        Card card = cardFinderUtil.findById(id);
        return CardResponseMapper.map(card);
    }
}

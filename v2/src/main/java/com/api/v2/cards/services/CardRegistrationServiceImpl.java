package com.api.v2.cards.services;

import com.api.v2.cards.domain.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.dtos.CardRegistrationDto;
import com.api.v2.cards.dtos.CardResponseDto;
import com.api.v2.cards.util.CardResponseMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CardRegistrationServiceImpl implements CardRegistrationService {

    private final CardRepository cardRepository;

    public CardRegistrationServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public CardResponseDto registerCreditCard(@Valid CardRegistrationDto registrationDto) {
        Card card = Card.create("credit card", registrationDto);
        Card savedCard = cardRepository.save(card);
        return CardResponseMapper.map(savedCard);
    }

    @Override
    public CardResponseDto registerDebitCard(@Valid CardRegistrationDto registrationDto) {
        Card card = Card.create("debit card", registrationDto);
        Card savedCard = cardRepository.save(card);
        return CardResponseMapper.map(savedCard);
    }
}

package com.api.v2.cards.util;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.exceptions.NonExistentCardException;
import org.springframework.stereotype.Component;

@Component
public class CardFinderUtil {

    private final CardRepository cardRepository;

    public CardFinderUtil(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card findById(String id) {
        return cardRepository
                .findById(id)
                .orElseThrow(() -> new NonExistentCardException(id));
    }
}

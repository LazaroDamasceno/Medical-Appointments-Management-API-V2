package com.api.v2.cards.services;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.util.CardFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CardDeletionServiceImpl implements CardDeletionService {

    private final CardFinder cardFinder;
    private final CardRepository cardRepository;

    public CardDeletionServiceImpl(CardFinder cardFinder,
                                   CardRepository cardRepository
    ) {
        this.cardFinder = cardFinder;
        this.cardRepository = cardRepository;
    }

    @Override
    public ResponseEntity<Void> deleteById(String id) {
        Card card = cardFinder.findById(id);
        cardRepository.delete(card);
        return ResponseEntity.noContent().build();
    }
}

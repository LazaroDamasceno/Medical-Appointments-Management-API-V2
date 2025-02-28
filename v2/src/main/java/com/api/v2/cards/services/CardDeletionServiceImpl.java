package com.api.v2.cards.services;

import com.api.v2.cards.domain.Card;
import com.api.v2.cards.domain.CardRepository;
import com.api.v2.cards.util.CardFinderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CardDeletionServiceImpl implements CardDeletionService {

    private final CardFinderUtil cardFinderUtil;
    private final CardRepository cardRepository;

    public CardDeletionServiceImpl(CardFinderUtil cardFinderUtil,
                                   CardRepository cardRepository
    ) {
        this.cardFinderUtil = cardFinderUtil;
        this.cardRepository = cardRepository;
    }

    @Override
    public ResponseEntity<Void> deleteById(String id) {
        Card card = cardFinderUtil.findById(id);
        cardRepository.delete(card);
        return ResponseEntity.noContent().build();
    }
}

package com.api.v2.cards.util;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.dtos.CardResponseDto;

public final class CardResponseMapper {
    public static CardResponseDto map(Card card) {
        return new CardResponseDto(
                card.id(),
                card.type(),
                card.bank(),
                card.cvc_cvv(),
                card.dueDate()
        );
    }
}

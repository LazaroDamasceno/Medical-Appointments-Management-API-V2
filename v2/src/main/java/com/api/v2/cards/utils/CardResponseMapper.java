package com.api.v2.cards.utils;

import com.api.v2.cards.domain.exposed.Card;
import com.api.v2.cards.responses.CardResponseDto;

public final class CardResponseMapper {
    public static CardResponseDto toDto(Card card) {
        return new CardResponseDto(
                card.id(),
                card.type(),
                card.bank(),
                card.cvc_cvv(),
                card.dueDate()
        );
    }
}

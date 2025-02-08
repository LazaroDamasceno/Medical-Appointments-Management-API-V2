package com.api.v2.cards.services;

import com.api.v2.cards.dtos.CardRegistrationDto;
import com.api.v2.cards.dtos.CardResponseDto;

public interface CardRegistrationService {
    CardResponseDto registerCreditCard(CardRegistrationDto registrationDto);
    CardResponseDto registerDebitCard(CardRegistrationDto registrationDto);
}

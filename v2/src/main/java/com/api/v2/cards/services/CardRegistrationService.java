package com.api.v2.cards.services;

import com.api.v2.cards.requests.CardRegistrationDto;
import com.api.v2.cards.responses.CardResponseDto;
import org.springframework.http.ResponseEntity;

public interface CardRegistrationService {
    ResponseEntity<CardResponseDto> registerCreditCard(CardRegistrationDto registrationDto);
    ResponseEntity<CardResponseDto> registerDebitCard(CardRegistrationDto registrationDto);
}

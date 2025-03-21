package com.api.v2.cards.services;

import com.api.v2.cards.dtos.CardRegistrationDto;
import com.api.v2.cards.dtos.exposed.CardResponseDto;
import org.springframework.http.ResponseEntity;

public interface CardRegistrationService {
    ResponseEntity<CardResponseDto> registerCreditCard(CardRegistrationDto registrationDto);
    ResponseEntity<CardResponseDto> registerDebitCard(CardRegistrationDto registrationDto);
}

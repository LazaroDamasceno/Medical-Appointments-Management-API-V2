package com.api.v2.cards.controllers;

import com.api.v2.cards.dtos.CardRegistrationDto;
import com.api.v2.cards.dtos.CardResponseDto;
import com.api.v2.cards.services.CardDeletionService;
import com.api.v2.cards.services.CardRegistrationService;
import com.api.v2.cards.services.CardRetrievalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/cards")
public class CardController {

    private final CardRegistrationService registrationService;
    private final CardRetrievalService retrievalService;
    private final CardDeletionService deletionService;

    public CardController(CardRegistrationService registrationService,
                          CardRetrievalService retrievalService,
                          CardDeletionService deletionService
    ) {
        this.registrationService = registrationService;
        this.retrievalService = retrievalService;
        this.deletionService = deletionService;
    }


    @PostMapping("credit-card")
    public ResponseEntity<CardResponseDto> registerCreditCard(@Valid @RequestBody CardRegistrationDto registrationDto) {
        return registrationService.registerCreditCard(registrationDto);
    }

    @PostMapping("debit-card")
    public ResponseEntity<CardResponseDto> registerDebitCard(@Valid @RequestBody CardRegistrationDto registrationDto) {
        return registrationService.registerDebitCard(registrationDto);
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDto>> findAll() {
        return retrievalService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CardResponseDto> findById(@PathVariable String id) {
        return retrievalService.findById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return deletionService.deleteById(id);
    }
}

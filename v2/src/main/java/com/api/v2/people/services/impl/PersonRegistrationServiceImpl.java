package com.api.v2.people.services.impl;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.domain.PersonRepository;
import com.api.v2.people.dtos.PersonRegistrationDto;
import com.api.v2.people.services.interfaces.PersonRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PersonRegistrationServiceImpl implements PersonRegistrationService {

    private final PersonRepository personRepository;

    public PersonRegistrationServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person register(@Valid PersonRegistrationDto registrationDto) {
        return personRepository
                .findAll()
                .stream()
                .filter(p -> p.getEmail().equals(registrationDto.email()))
                .findAny()
                .orElseGet(() -> {
                    Person person = Person.of(registrationDto);
                    return personRepository.save(person);
                });
    }
}

package com.api.v3.people.services.impl;

import com.api.v3.people.domain.Person;
import com.api.v3.people.domain.PersonRepository;
import com.api.v3.people.dtos.PersonRegistrationDto;
import com.api.v3.people.services.interfaces.PersonRegistrationService;
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
        Person person = Person.create(registrationDto);
        return personRepository.save(person);
    }
}

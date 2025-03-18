package com.api.v2.common;

import com.api.v2.people.domain.PersonRepository;
import org.springframework.stereotype.Component;

@Component
public class DuplicatedPersonalDataChecker {

    private final PersonRepository personRepository;

    public DuplicatedPersonalDataChecker(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public boolean isSsnDuplicated(String ssn) {
        return personRepository
                .findAll()
                .stream()
                .anyMatch(d -> d.getSsn().equals(ssn));
    }

    public boolean isEmailDuplicated(String email) {
        return personRepository
                .findAll()
                .stream()
                .anyMatch(d -> d.getEmail().equals(email));
    }

}

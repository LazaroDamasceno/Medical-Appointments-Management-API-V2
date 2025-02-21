package com.api.v2.people.services.impl;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.domain.PersonAuditTrail;
import com.api.v2.people.domain.PersonAuditTrailRepository;
import com.api.v2.people.domain.PersonRepository;
import com.api.v2.people.dtos.PersonModificationDto;
import com.api.v2.people.services.interfaces.PersonModificationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PersonModificationServiceImpl implements PersonModificationService {

    private final PersonRepository personRepository;
    private final PersonAuditTrailRepository personAuditTrailRepository;

    public PersonModificationServiceImpl(PersonRepository personRepository,
                                         PersonAuditTrailRepository personAuditTrailRepository
    ) {
        this.personRepository = personRepository;
        this.personAuditTrailRepository = personAuditTrailRepository;
    }

    @Override
    public Person modify(Person person, @Valid PersonModificationDto modificationDto) {
        PersonAuditTrail personAuditTrail = PersonAuditTrail.of(person);
        personAuditTrailRepository.save(personAuditTrail);
        person.modify(modificationDto);
        return personRepository.save(person);
    }
}

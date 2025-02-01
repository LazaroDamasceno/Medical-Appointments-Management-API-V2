package com.api.v3.people.services.impl;

import com.api.v3.people.domain.Person;
import com.api.v3.people.domain.PersonAuditTrail;
import com.api.v3.people.domain.PersonAuditTrailRepository;
import com.api.v3.people.domain.PersonRepository;
import com.api.v3.people.dtos.PersonModificationDto;
import com.api.v3.people.services.interfaces.PersonModificationService;
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
        PersonAuditTrail personAuditTrail = PersonAuditTrail.create(person);
        personAuditTrailRepository.save(personAuditTrail);
        person.modify(modificationDto);
        return personRepository.save(person);
    }
}

package com.api.v2.people.services.interfaces;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.dtos.PersonModificationDto;

public interface PersonModificationService {
    Person modify(Person person, PersonModificationDto modificationDto);
}

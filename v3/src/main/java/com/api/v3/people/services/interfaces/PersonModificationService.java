package com.api.v3.people.services.interfaces;

import com.api.v3.people.domain.Person;
import com.api.v3.people.dtos.PersonModificationDto;

public interface PersonModificationService {
    Person modify(Person person, PersonModificationDto modificationDto);
}

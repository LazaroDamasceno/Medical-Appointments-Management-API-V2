package com.api.v2.people.services.interfaces;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.dtos.PersonRegistrationDto;

public interface PersonRegistrationService {
    Person register(PersonRegistrationDto registrationDto);
}

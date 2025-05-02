package com.api.v2.people.services.interfaces;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.requests.PersonRegistrationDto;

public interface PersonRegistrationService {
    Person register(PersonRegistrationDto registrationDto);
}

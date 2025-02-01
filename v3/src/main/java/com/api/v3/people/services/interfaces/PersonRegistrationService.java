package com.api.v3.people.services.interfaces;

import com.api.v3.people.domain.Person;
import com.api.v3.people.dtos.PersonRegistrationDto;

public interface PersonRegistrationService {
    Person register(PersonRegistrationDto registrationDto);
}

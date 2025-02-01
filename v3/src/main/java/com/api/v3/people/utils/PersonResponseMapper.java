package com.api.v3.people.utils;

import com.api.v3.people.domain.Person;
import com.api.v3.people.dtos.PersonResponseDto;

public class PersonResponseMapper {

    public static PersonResponseDto map(Person person) {
        return new PersonResponseDto(
                person.getFullName(),
                person.getId().toString()
        );
    }
}

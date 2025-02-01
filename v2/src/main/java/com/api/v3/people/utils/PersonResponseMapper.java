package com.api.v2.people.utils;

import com.api.v2.people.domain.Person;
import com.api.v2.people.dtos.PersonResponseDto;

public class PersonResponseMapper {

    public static PersonResponseDto map(Person person) {
        return new PersonResponseDto(
                person.getFullName(),
                person.getId().toString()
        );
    }
}

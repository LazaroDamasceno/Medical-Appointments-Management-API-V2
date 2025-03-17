package com.api.v2.people.utils;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.dtos.PersonResponseDto;

public class PersonResponseMapper {

    public static PersonResponseDto mapToResource(Person person) {
        return new PersonResponseDto(
                person.getFullName(),
                person.getId()
        );
    }
}

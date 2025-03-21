package com.api.v2.people.utils;

import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.dtos.PersonResponseDto;

public final class PersonResponseMapper {

    public static PersonResponseDto mapToResource(Person person) {
        return new PersonResponseDto(
                FullNameFormatter.format(person),
                person.getId()
        );
    }
}

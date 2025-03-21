package com.api.v2.people.utils;

import com.api.v2.people.domain.exposed.Person;

public final class FullNameFormatter {

    public static String format(Person person) {
        if (person.getMiddleName() == null || person.getMiddleName().isBlank()) {
            return "%s %s".formatted(person.getFirstName(), person.getLastName());
        }
        return "%s %s %s".formatted(person.getFirstName(), person.getMiddleName(), person.getLastName());
    }
}

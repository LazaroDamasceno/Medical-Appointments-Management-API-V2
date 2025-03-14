package com.api.v2.people.domain;

import com.api.v2.people.domain.exposed.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
}

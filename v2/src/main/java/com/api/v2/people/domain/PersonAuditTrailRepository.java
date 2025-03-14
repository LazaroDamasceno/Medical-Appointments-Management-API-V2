package com.api.v2.people.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonAuditTrailRepository extends MongoRepository<PersonAuditTrail, String> {
}

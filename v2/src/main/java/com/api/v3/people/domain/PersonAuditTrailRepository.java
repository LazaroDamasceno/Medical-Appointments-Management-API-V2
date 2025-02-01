package com.api.v2.people.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonAuditTrailRepository extends MongoRepository<PersonAuditTrail, ObjectId> {
}

package com.api.v2.doctors.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorAuditTrailRepository extends MongoRepository<DoctorAuditTrail, ObjectId> {
}

package com.api.v2.doctors.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorAuditTrailRepository extends MongoRepository<DoctorAuditTrail, String> {
}

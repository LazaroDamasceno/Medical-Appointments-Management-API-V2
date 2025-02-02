package com.api.v2.doctors.domain;

import com.api.v2.doctors.domain.exposed.Doctor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, ObjectId> {
}

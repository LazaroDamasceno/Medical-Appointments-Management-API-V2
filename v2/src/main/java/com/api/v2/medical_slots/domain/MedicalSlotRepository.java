package com.api.v2.medical_slots.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicalSlotRepository extends MongoRepository<MedicalSlot, ObjectId> {
}

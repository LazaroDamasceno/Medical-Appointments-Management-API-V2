package com.api.v2.medical_appointments;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicalAppointmentRepository extends MongoRepository<MedicalAppointment, ObjectId> {
}

package com.api.v2.medical_appointments.domain;

import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicalAppointmentRepository extends MongoRepository<MedicalAppointment, String> {
}

package com.api.v2.medical_slots.domain;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Document
public class MedicalSlot {

    @BsonId
    private ObjectId id;
    private Doctor doctor;
    private MedicalAppointment medicalAppointment;
    private LocalDateTime availableAt;
    private ZoneId availableAtZoneId;
    private ZoneOffset availableAtZoneOffset;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private LocalDateTime canceledAt;
    private ZoneId canceledAtZoneId;
    private ZoneOffset canceledAtZoneOffset;
    private LocalDateTime completedAt;
    private ZoneId completedAtZoneId;
    private ZoneOffset completedAtZoneOffset;

    private MedicalSlot(Doctor doctor, LocalDateTime availableAt, ZoneId availableAtZoneId, ZoneOffset availableAtZoneOffset) {
        this.id = new ObjectId();
        this.doctor = doctor;
        this.availableAt = availableAt;
        this.availableAtZoneId = availableAtZoneId;
        this.availableAtZoneOffset = availableAtZoneOffset;
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
    }

    public static MedicalSlot create(Doctor doctor,
                                     LocalDateTime availableAt,
                                     ZoneId availableAtZoneId,
                                     ZoneOffset availableAtZoneOffset
    ) {
        return new MedicalSlot(doctor, availableAt, availableAtZoneId, availableAtZoneOffset);
    }

    public ObjectId getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getAvailableAt() {
        return availableAt;
    }

    public ZoneId getAvailableAtZoneId() {
        return availableAtZoneId;
    }

    public ZoneOffset getAvailableAtZoneOffset() {
        return availableAtZoneOffset;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ZoneId getCreatedAtZoneId() {
        return createdAtZoneId;
    }

    public ZoneOffset getCreatedAtZoneOffset() {
        return createdAtZoneOffset;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public ZoneId getCanceledAtZoneId() {
        return canceledAtZoneId;
    }

    public ZoneOffset getCanceledAtZoneOffset() {
        return canceledAtZoneOffset;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public ZoneId getCompletedAtZoneId() {
        return completedAtZoneId;
    }

    public ZoneOffset getCompletedAtZoneOffset() {
        return completedAtZoneOffset;
    }

    public MedicalAppointment getMedicalAppointment() {
        return medicalAppointment;
    }

    public void setMedicalAppointment(MedicalAppointment medicalAppointment) {
        this.medicalAppointment = medicalAppointment;
    }

    public void markAsCanceled() {
        canceledAt = LocalDateTime.now();
        canceledAtZoneId = ZoneId.systemDefault();
        canceledAtZoneOffset = OffsetDateTime.now().getOffset();

    }

    public void markAsCompleted() {
        completedAt = LocalDateTime.now();
        completedAtZoneId = ZoneId.systemDefault();
        completedAtZoneOffset = OffsetDateTime.now().getOffset();
    }
}

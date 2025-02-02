package com.api.v2.medical_slots.domain;

import com.api.v2.doctors.domain.Doctor;
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

    public MedicalSlot() {
    }

    private MedicalSlot(Doctor doctor, LocalDateTime availableAt) {
        this.id = new ObjectId();
        this.doctor = doctor;
        this.availableAt = availableAt;
        this.availableAtZoneId = ZoneId.systemDefault();
        this.availableAtZoneOffset = OffsetDateTime
                .ofInstant(availableAt.toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
                .getOffset();
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
    }

    public static MedicalSlot create(Doctor doctor, LocalDateTime availableAt) {
        return new MedicalSlot(doctor, availableAt);
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
}

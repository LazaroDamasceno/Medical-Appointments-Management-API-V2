package com.api.v2.doctors.domain;

import com.api.v2.people.domain.Person;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Document
public class Doctor {

    @BsonId
    private ObjectId id;
    private Person person;
    private String medicalLicenseNumber;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private LocalDateTime modifiedAt;
    private ZoneId modifiedAtZoneId;
    private ZoneOffset modifiedAtZoneOffset;

    public Doctor() {
    }

    public Doctor(Person person, String medicalLicenseNumber) {
        this.id = new ObjectId();
        this.person = person;
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
    }

    public ObjectId getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
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

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public ZoneId getModifiedAtZoneId() {
        return modifiedAtZoneId;
    }

    public ZoneOffset getModifiedAtZoneOffset() {
        return modifiedAtZoneOffset;
    }
}

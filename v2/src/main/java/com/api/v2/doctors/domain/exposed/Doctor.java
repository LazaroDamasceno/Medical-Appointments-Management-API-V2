package com.api.v2.doctors.domain.exposed;

import com.api.v2.common.DstCheckerUtil;
import com.api.v2.people.domain.exposed.Person;
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
    private final String medicalLicenseNumber;
    private final LocalDateTime hiredAt;
    private final ZoneId hiredAtZoneId;
    private final ZoneOffset hiredAtZoneOffset;
    private final boolean isHiredDuringDST;
    private LocalDateTime terminatedAt;
    private ZoneId terminatedAtZoneId;
    private ZoneOffset terminatedAtZoneOffset;
    private Boolean isTerminatedDuringDST;

    private Doctor(Person person, String medicalLicenseNumber) {
        this.id = new ObjectId();
        this.person = person;
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.hiredAt = LocalDateTime.now();
        this.hiredAtZoneId = ZoneId.systemDefault();
        this.hiredAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isHiredDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public static Doctor create(Person person, String medicalLicenseNumber) {
        return new Doctor(person, medicalLicenseNumber);
    }

    public void markAsTerminated() {
        terminatedAt = LocalDateTime.now();
        terminatedAtZoneId = ZoneId.systemDefault();
        terminatedAtZoneOffset = OffsetDateTime.now().getOffset();
        isTerminatedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(terminatedAt, terminatedAtZoneId);
    }

    public void markAsRehired() {
        terminatedAt = null;
        terminatedAtZoneId = null;
        terminatedAtZoneOffset = null;
        isTerminatedDuringDST = null;
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

    public LocalDateTime getHiredAt() {
        return hiredAt;
    }

    public ZoneId getHiredAtZoneId() {
        return hiredAtZoneId;
    }

    public ZoneOffset getHiredAtZoneOffset() {
        return hiredAtZoneOffset;
    }

    public LocalDateTime getTerminatedAt() {
        return terminatedAt;
    }

    public ZoneId getTerminatedAtZoneId() {
        return terminatedAtZoneId;
    }

    public ZoneOffset getTerminatedAtZoneOffset() {
        return terminatedAtZoneOffset;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isHiredDuringDST() {
        return isHiredDuringDST;
    }

    public boolean isTerminatedDuringDST() {
        return isTerminatedDuringDST;
    }
}

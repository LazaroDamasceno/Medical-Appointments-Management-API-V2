package com.api.v2.doctors.domain.exposed;

import com.api.v2.common.DstCheckerUtil;
import com.api.v2.people.domain.exposed.Person;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Document
public class Doctor {

    @Id
    private String id;
    private Person person;
    private String medicalLicenseNumber;
    private LocalDateTime hiredAt;
    private ZoneId hiredAtZoneId;
    private ZoneOffset hiredAtZoneOffset;
    private boolean isHiredDuringDST;
    private LocalDateTime terminatedAt;
    private ZoneId terminatedAtZoneId;
    private ZoneOffset terminatedAtZoneOffset;
    private Boolean isTerminatedDuringDST;

    public Doctor() {
    }

    private Doctor(Person person, String medicalLicenseNumber) {
        this.id = UUID.randomUUID().toString();
        this.person = person;
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.hiredAt = LocalDateTime.now();
        this.hiredAtZoneId = ZoneId.systemDefault();
        this.hiredAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isHiredDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public static Doctor of(Person person, String medicalLicenseNumber) {
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

    public String getId() {
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

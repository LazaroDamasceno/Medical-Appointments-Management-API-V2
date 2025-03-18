package com.api.v2.people.domain.exposed;

import com.api.v2.common.DstChecker;
import com.api.v2.people.dtos.PersonRegistrationDto;
import com.api.v2.people.utils.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;
import java.util.UUID;

@Document
public class Person {

    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private String ssn;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private Boolean isCreatedDuringDST;
    private LocalDateTime modifiedAt;
    private ZoneId modifiedAtZoneId;
    private ZoneOffset modifiedAtZoneOffset;
    private Boolean isModifiedDuringDST;

    private Person(PersonRegistrationDto registrationDto) {
        this.id = UUID.randomUUID().toString();
        this.firstName = registrationDto.firstName();
        this.middleName = registrationDto.middleName();
        this.lastName = registrationDto.lastName();
        this.birthDate = registrationDto.birthDate();
        this.ssn = registrationDto.ssn();
        this.email = registrationDto.email();
        this.phoneNumber = registrationDto.phoneNumber();
        this.gender = registrationDto.gender();
        this.createdAt = LocalDateTime.now(ZoneId.systemDefault());
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isCreatedDuringDST = DstChecker.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public static Person of(PersonRegistrationDto registrationDto) {
        return new Person(registrationDto);
    }

    public String getFullName() {
        if (middleName.isBlank()) {
            return "%s %s".formatted(firstName, lastName);
        }
        return "%s %s %s".formatted(firstName, middleName, lastName);
    }

    public String getId() {
        return id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getSsn() {
        return ssn;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ZoneId getCreatedAtZoneId() {
        return createdAtZoneId;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public ZoneId getModifiedAtZoneId() {
        return modifiedAtZoneId;
    }

    public ZoneOffset getCreatedAtZoneOffset() {
        return createdAtZoneOffset;
    }

    public ZoneOffset getModifiedAtZoneOffset() {
        return modifiedAtZoneOffset;
    }

    public boolean isCreatedDuringDST() {
        return isCreatedDuringDST;
    }

    public boolean isModifiedDuringDST() {
        return isModifiedDuringDST;
    }
}

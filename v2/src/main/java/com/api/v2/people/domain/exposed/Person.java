package com.api.v2.people.domain.exposed;

import com.api.v2.common.DstCheckerUtil;
import com.api.v2.people.dtos.PersonModificationDto;
import com.api.v2.people.dtos.PersonRegistrationDto;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;

@Document
public class Person {

    @BsonId
    private ObjectId id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private String ssn;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private boolean isCreatedDuringDST;
    private LocalDateTime modifiedAt;
    private ZoneId modifiedAtZoneId;
    private ZoneOffset modifiedAtZoneOffset;
    private boolean isModifiedDuringDST;

    public Person() {
    }

    private Person(PersonRegistrationDto registrationDto) {
        this.id = new ObjectId();
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
        this.isCreatedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public static Person of(PersonRegistrationDto registrationDto) {
        return new Person(registrationDto);
    }

    public void modify(PersonModificationDto modificationDto) {
        this.firstName = modificationDto.firstName();
        this.middleName = modificationDto.middleName();
        this.lastName = modificationDto.lastName();
        this.birthDate = modificationDto.birthDate();
        this.email = modificationDto.email();
        this.phoneNumber = modificationDto.phoneNumber();
        this.gender = modificationDto.gender();
        this.modifiedAt = LocalDateTime.now(ZoneId.systemDefault());
        this.modifiedAtZoneId = ZoneId.systemDefault();
        this.modifiedAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isModifiedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public String getFullName() {
        if (middleName.isEmpty()) {
            return "%s %s".formatted(firstName, lastName);
        }
        return "%s %s %s".formatted(firstName, middleName, lastName);
    }

    public ObjectId getId() {
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

    public String getGender() {
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

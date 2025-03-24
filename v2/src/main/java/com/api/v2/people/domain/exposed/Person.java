package com.api.v2.people.domain.exposed;

import com.api.v2.common.DstChecker;
import com.api.v2.people.dtos.PersonRegistrationDto;
import com.api.v2.people.enums.Gender;
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

    public Person() {
    }

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

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
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

    public ZoneOffset getCreatedAtZoneOffset() {
        return createdAtZoneOffset;
    }

    public Boolean getCreatedDuringDST() {
        return isCreatedDuringDST;
    }
}

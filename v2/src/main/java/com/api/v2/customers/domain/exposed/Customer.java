package com.api.v2.customers.domain.exposed;

import com.api.v2.common.Address;
import com.api.v2.common.DstChecker;
import com.api.v2.customers.responses.CustomerResponseDto;
import com.api.v2.people.domain.exposed.Person;
import com.api.v2.people.utils.FullNameFormatter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Document
public class Customer {

    @Id
    private String id;
    private Address address;
    private Person person;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private Boolean isCreatedDuringDST;

    public Customer() {
    }

    private Customer(Address address, Person person) {
        this.id = UUID.randomUUID().toString();
        this.address = address;
        this.person = person;
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isCreatedDuringDST = DstChecker.isGivenDateTimeFollowingDst(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public static Customer of(Address address, Person person) {
        return new Customer(address, person);
    }

    public CustomerResponseDto toDto() {
        return new CustomerResponseDto(
                FullNameFormatter.format(person),
                address
        );
    }

    public String getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public Person getPerson() {
        return person;
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

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean isCreatedDuringDST() {
        return isCreatedDuringDST;
    }
}

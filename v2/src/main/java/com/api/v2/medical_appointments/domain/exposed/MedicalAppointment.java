package com.api.v2.medical_appointments.domain.exposed;

import com.api.v2.common.DstChecker;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.responses.SimplifiedMedicalAppointmentResponseDto;
import com.api.v2.medical_appointments.enums.MedicalAppointmentType;
import com.api.v2.medical_appointments.responses.MedicalAppointmentResponseResource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;
import java.util.UUID;

@Document
public class MedicalAppointment {

    @Id
    private String id;
    private MedicalAppointmentType type;
    private Customer customer;
    private Doctor doctor;
    private LocalDateTime bookedAt;
    private ZoneId bookedAtZoneId;
    private ZoneOffset bookedAtZoneOffset;
    private Boolean isBookedDuringDST;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private Boolean isCreatedDuringDST;
    private LocalDateTime canceledAt;
    private ZoneId canceledAtZoneId;
    private ZoneOffset canceledAtZoneOffset;
    private Boolean isCanceledDuringDST;
    private LocalDateTime completedAt;
    private ZoneId completedAtZoneId;
    private ZoneOffset completedAtZoneOffset;
    private Boolean isCompletedDuringDST;
    private LocalDateTime paidAt;
    private ZoneId paidAtZoneId;
    private ZoneOffset paidAtZoneOffset;
    private Boolean isPaymentDuringDST;

    public MedicalAppointment() {
    }

    private MedicalAppointment(MedicalAppointmentType type,
                               Customer customer,
                               Doctor doctor,
                               LocalDateTime bookedAt,
                               ZoneId bookedAtZoneId,
                               ZoneOffset bookedAtZoneOffset
    ) {
        this.type = type;
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.doctor = doctor;
        this.bookedAt = bookedAt;
        this.bookedAtZoneId = bookedAtZoneId;
        this.bookedAtZoneOffset = bookedAtZoneOffset;
        this.isBookedDuringDST = DstChecker.isGivenDateTimeFollowingDst(bookedAt, bookedAtZoneId);
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
        isCreatedDuringDST = DstChecker.isGivenDateTimeFollowingDst(createdAt, createdAtZoneId);
    }

    public static MedicalAppointment of(MedicalAppointmentType type,
                                        Customer customer,
                                        Doctor doctor,
                                        LocalDateTime bookedAt,
                                        ZoneId bookedAtZoneId,
                                        ZoneOffset bookedAtZoneOffset
    ) {
        return new MedicalAppointment(type, customer, doctor, bookedAt, bookedAtZoneId, bookedAtZoneOffset);
    }

    public void markAsCanceled() {
        canceledAt = LocalDateTime.now();
        canceledAtZoneId = ZoneId.systemDefault();
        canceledAtZoneOffset = OffsetDateTime.now().getOffset();
        isCanceledDuringDST = DstChecker.isGivenDateTimeFollowingDst(canceledAt, canceledAtZoneId);
    }

    public void markAsCompleted() {
        completedAt = LocalDateTime.now();
        completedAtZoneId = ZoneId.systemDefault();
        completedAtZoneOffset = OffsetDateTime.now().getOffset();
        isCompletedDuringDST = DstChecker.isGivenDateTimeFollowingDst(completedAt, completedAtZoneId);
    }

    public void markAsPaid() {
        paidAt = LocalDateTime.now();
        paidAtZoneId = ZoneOffset.systemDefault();
        paidAtZoneOffset = OffsetDateTime.now().getOffset();
        isPaymentDuringDST = DstChecker.isGivenDateTimeFollowingDst(paidAt, paidAtZoneId);
    }

    public SimplifiedMedicalAppointmentResponseDto toDto() {
        return new SimplifiedMedicalAppointmentResponseDto(
                id,
                type,
                customer.toDto(),
                "%s%s[%s]".formatted(bookedAt, bookedAtZoneOffset, bookedAtZoneId),
                "%s%s[%s]".formatted(canceledAt, canceledAtZoneOffset, canceledAtZoneId),
                "%s%s[%s]".formatted(completedAt, completedAtZoneOffset, completedAtZoneId)
        );
    }

    public MedicalAppointmentResponseResource toResource() {
        return new MedicalAppointmentResponseResource(
                id,
                type,
                customer.toDto(),
                doctor.toResource(),
                "%s%s[%s]".formatted(bookedAt, bookedAtZoneOffset, bookedAtZoneId),
                "%s%s[%s]".formatted(canceledAt, canceledAtZoneOffset, canceledAtZoneId),
                "%s%s[%s]".formatted(completedAt, completedAtZoneOffset, completedAtZoneId)
        );
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public ZoneId getBookedAtZoneId() {
        return bookedAtZoneId;
    }

    public ZoneOffset getBookedAtZoneOffset() {
        return bookedAtZoneOffset;
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

    public Boolean isBookedDuringDST() {
        return isBookedDuringDST;
    }

    public Boolean isCompletedDuringDST() {
        return isCompletedDuringDST;
    }

    public Boolean isCanceledDuringDST() {
        return isCanceledDuringDST;
    }

    public Boolean isCreatedDuringDST() {
        return isCreatedDuringDST;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public ZoneId getPaidAtZoneId() {
        return paidAtZoneId;
    }

    public ZoneOffset getPaidAtZoneOffset() {
        return paidAtZoneOffset;
    }

    public Boolean isPaymentDuringDST() {
        return isPaymentDuringDST;
    }

    public MedicalAppointmentType getType() {
        return type;
    }

    public Boolean getBookedDuringDST() {
        return isBookedDuringDST;
    }

    public Boolean getCreatedDuringDST() {
        return isCreatedDuringDST;
    }

    public Boolean getCanceledDuringDST() {
        return isCanceledDuringDST;
    }

    public Boolean getCompletedDuringDST() {
        return isCompletedDuringDST;
    }

    public Boolean getPaymentDuringDST() {
        return isPaymentDuringDST;
    }
}

package com.api.v2.medical_appointments.domain.exposed;

import com.api.v2.common.DstCheckerUtil;
import com.api.v2.customers.domain.exposed.Customer;
import com.api.v2.doctors.domain.exposed.Doctor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;

@Document
public class MedicalAppointment {

    @BsonId
    private ObjectId id;
    private Customer customer;
    private Doctor doctor;
    private LocalDateTime bookedAt;
    private ZoneId bookedAtZoneId;
    private ZoneOffset bookedAtZoneOffset;
    private boolean isBookedDuringDST;
    private LocalDateTime createdAt;
    private ZoneId createdAtZoneId;
    private ZoneOffset createdAtZoneOffset;
    private boolean isCreatedDuringDST;
    private LocalDateTime canceledAt;
    private ZoneId canceledAtZoneId;
    private ZoneOffset canceledAtZoneOffset;
    private boolean isCanceledDuringDST;
    private LocalDateTime completedAt;
    private ZoneId completedAtZoneId;
    private ZoneOffset completedAtZoneOffset;
    private boolean isCompletedDuringDST;
    private LocalDateTime paidAt;
    private ZoneId paidAtZoneId;
    private ZoneOffset paidAtZoneOffset;
    private boolean isPaymentDuringDST;

    private MedicalAppointment(Customer customer,
                               Doctor doctor,
                               LocalDateTime bookedAt,
                               ZoneId bookedAtZoneId,
                               ZoneOffset bookedAtZoneOffset
    ) {
        this.id = new ObjectId();
        this.customer = customer;
        this.doctor = doctor;
        this.bookedAt = bookedAt;
        this.bookedAtZoneId = bookedAtZoneId;
        this.bookedAtZoneOffset = bookedAtZoneOffset;
        this.isBookedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(bookedAt, bookedAtZoneId);
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
        isCreatedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(createdAt, createdAtZoneId);
    }

    public MedicalAppointment() {
    }

    public static MedicalAppointment of(Customer customer,
                                        Doctor doctor,
                                        LocalDateTime bookedAt,
                                        ZoneId bookedAtZoneId,
                                        ZoneOffset bookedAtZoneOffset
    ) {
        return new MedicalAppointment(customer, doctor, bookedAt, bookedAtZoneId, bookedAtZoneOffset);
    }

    public void markAsCanceled() {
        canceledAt = LocalDateTime.now();
        canceledAtZoneId = ZoneId.systemDefault();
        canceledAtZoneOffset = OffsetDateTime.now().getOffset();
        isCanceledDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(canceledAt, canceledAtZoneId);
    }

    public void markAsCompleted() {
        completedAt = LocalDateTime.now();
        completedAtZoneId = ZoneId.systemDefault();
        completedAtZoneOffset = OffsetDateTime.now().getOffset();
        isCompletedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(completedAt, completedAtZoneId);
    }

    public void markAsPaid() {
        paidAt = LocalDateTime.now();
        paidAtZoneId = ZoneOffset.systemDefault();
        paidAtZoneOffset = OffsetDateTime.now().getOffset();
        isPaymentDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(paidAt, paidAtZoneId);
    }

    public ObjectId getId() {
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

    public boolean isBookedDuringDST() {
        return isBookedDuringDST;
    }

    public boolean isCompletedDuringDST() {
        return isCompletedDuringDST;
    }

    public boolean isCanceledDuringDST() {
        return isCanceledDuringDST;
    }

    public boolean isCreatedDuringDST() {
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

    public boolean isPaymentDuringDST() {
        return isPaymentDuringDST;
    }
}

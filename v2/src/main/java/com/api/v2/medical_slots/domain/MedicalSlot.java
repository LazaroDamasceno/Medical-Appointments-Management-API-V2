package com.api.v2.medical_slots.domain;

import com.api.v2.common.DstCheckerUtil;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
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
    private final Doctor doctor;
    private MedicalAppointment medicalAppointment;
    private final LocalDateTime availableAt;
    private final ZoneId availableAtZoneId;
    private final ZoneOffset availableAtZoneOffset;
    private final LocalDateTime createdAt;
    private final ZoneId createdAtZoneId;
    private final ZoneOffset createdAtZoneOffset;
    private final boolean isCreatedDuringDST;
    private LocalDateTime canceledAt;
    private ZoneId canceledAtZoneId;
    private ZoneOffset canceledAtZoneOffset;
    private boolean isCanceledDuringDST;
    private LocalDateTime completedAt;
    private ZoneId completedAtZoneId;
    private ZoneOffset completedAtZoneOffset;
    private boolean isCompletedDuringDST;

    private MedicalSlot(Doctor doctor, LocalDateTime availableAt, ZoneId availableAtZoneId, ZoneOffset availableAtZoneOffset) {
        this.id = new ObjectId();
        this.doctor = doctor;
        this.availableAt = availableAt;
        this.availableAtZoneId = availableAtZoneId;
        this.availableAtZoneOffset = availableAtZoneOffset;
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isCreatedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(availableAt, availableAtZoneId);
    }

    public static MedicalSlot create(Doctor doctor,
                                     LocalDateTime availableAt,
                                     ZoneId availableAtZoneId,
                                     ZoneOffset availableAtZoneOffset
    ) {
        return new MedicalSlot(doctor, availableAt, availableAtZoneId, availableAtZoneOffset);
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
        isCompletedDuringDST = DstCheckerUtil.isGivenDateTimeFollowingDst(completedAt, canceledAtZoneId);
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

    public MedicalAppointment getMedicalAppointment() {
        return medicalAppointment;
    }

    public void setMedicalAppointment(MedicalAppointment medicalAppointment) {
        this.medicalAppointment = medicalAppointment;
    }

    public boolean isCreatedDuringDST() {
        return isCreatedDuringDST;
    }

    public boolean isCanceledDuringDST() {
        return isCanceledDuringDST;
    }

    public boolean isCompletedDuringDST() {
        return isCompletedDuringDST;
    }
}

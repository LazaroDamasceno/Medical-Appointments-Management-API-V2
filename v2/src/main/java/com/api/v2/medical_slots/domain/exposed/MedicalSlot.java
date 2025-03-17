package com.api.v2.medical_slots.domain.exposed;

import com.api.v2.common.DstChecker;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.medical_appointments.domain.exposed.MedicalAppointment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Document
public class MedicalSlot {

    @Id
    private String id;
    private Doctor doctor;
    private MedicalAppointment medicalAppointment;
    private LocalDateTime availableAt;
    private ZoneId availableAtZoneId;
    private ZoneOffset availableAtZoneOffset;
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

    private MedicalSlot(Doctor doctor, LocalDateTime availableAt, ZoneId availableAtZoneId, ZoneOffset availableAtZoneOffset) {
        this.id = UUID.randomUUID().toString();
        this.doctor = doctor;
        this.availableAt = availableAt;
        this.availableAtZoneId = availableAtZoneId;
        this.availableAtZoneOffset = availableAtZoneOffset;
        this.createdAt = LocalDateTime.now();
        this.createdAtZoneId = ZoneId.systemDefault();
        this.createdAtZoneOffset = OffsetDateTime.now().getOffset();
        this.isCreatedDuringDST = DstChecker.isGivenDateTimeFollowingDst(availableAt, availableAtZoneId);
    }

    public MedicalSlot() {
    }

    public void setMedicalAppointment(MedicalAppointment medicalAppointment) {
        this.medicalAppointment = medicalAppointment;
    }

    public static MedicalSlot of(Doctor doctor,
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
        isCanceledDuringDST = DstChecker.isGivenDateTimeFollowingDst(canceledAt, canceledAtZoneId);
        medicalAppointment = null;
    }

    public void markAsCompleted(MedicalAppointment completedMedicalAppointment) {
        completedAt = LocalDateTime.now();
        completedAtZoneId = ZoneId.systemDefault();
        completedAtZoneOffset = OffsetDateTime.now().getOffset();
        isCompletedDuringDST = DstChecker.isGivenDateTimeFollowingDst(completedAt, canceledAtZoneId);
        medicalAppointment = completedMedicalAppointment;
    }

    public String getId() {
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

    public Boolean isCreatedDuringDST() {
        return isCreatedDuringDST;
    }

    public Boolean isCanceledDuringDST() {
        return isCanceledDuringDST;
    }

    public Boolean isCompletedDuringDST() {
        return isCompletedDuringDST;
    }
}

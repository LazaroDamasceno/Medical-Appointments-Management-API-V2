package com.api.v2.medical_slots.exceptions;

public class InaccessibleMedicalSlotException extends RuntimeException {
    public InaccessibleMedicalSlotException(String medicalLicenseNumber, String slotId) {
        super("Doctor whose medicalLicenseNumber is %s is not associated with the medical slot whose id is %s"
                .formatted(medicalLicenseNumber, slotId));
    }
}

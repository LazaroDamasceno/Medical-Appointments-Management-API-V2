package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.resources.DoctorResponseResource;

public final class DoctorResponseMapper {
    public static DoctorResponseResource mapToResource(Doctor doctor) {
        return new DoctorResponseResource(
                doctor.getPerson().getFullName(),
                doctor.getMedicalLicenseNumber()
        );
    }
}

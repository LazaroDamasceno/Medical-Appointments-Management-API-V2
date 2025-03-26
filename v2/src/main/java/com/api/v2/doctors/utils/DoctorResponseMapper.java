package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.people.utils.FullNameFormatter;

public final class DoctorResponseMapper {
    public static DoctorResponseResource toResource(Doctor doctor) {
        return new DoctorResponseResource(
                FullNameFormatter.format(doctor.getPerson()),
                doctor.getMedicalLicenseNumber()
        );
    }
}

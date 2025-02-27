package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.resources.DoctorResponseResource;
import com.api.v2.people.utils.PersonResponseMapper;

public class DoctorResponseMapper {
    public static DoctorResponseResource mapToResource(Doctor doctor) {
        return new DoctorResponseResource(
                PersonResponseMapper.mapToResource(doctor.getPerson()),
                doctor.getMedicalLicenseNumber()
        );
    }
}

package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.dto.exposed.DoctorResponseDto;
import com.api.v2.people.utils.PersonResponseMapper;

public class DoctorResponseMapper {
    public static DoctorResponseDto mapToResource(Doctor doctor) {
        return new DoctorResponseDto(
                PersonResponseMapper.mapToResource(doctor.getPerson()),
                doctor.getMedicalLicenseNumber()
        );
    }
}

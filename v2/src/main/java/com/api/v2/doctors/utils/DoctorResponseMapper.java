package com.api.v2.doctors.utils;

import com.api.v2.doctors.domain.Doctor;
import com.api.v2.doctors.dto.DoctorResponseDto;
import com.api.v2.people.utils.PersonResponseMapper;

public class DoctorResponseMapper {
    public static DoctorResponseDto mapToDto(Doctor doctor) {
        return new DoctorResponseDto(
                PersonResponseMapper.mapToDto(doctor.getPerson()),
                doctor.getMedicalLicenseNumber()
        );
    }
}

package com.api.v2.doctors.utils;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.common.SuccessfulResponse;
import com.api.v2.doctors.domain.exposed.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DoctorFinderUtil {

    private final DoctorRepository doctorRepository;

    public DoctorFinderUtil(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Response<Doctor> findByMedicalLicenseNumber(String medicalLicenseNumber) {
        Optional<Doctor> optional = doctorRepository
                .findAll()
                .stream()
                .filter(d -> d.getMedicalLicenseNumber().equals(medicalLicenseNumber))
                .findFirst();
        if (optional.isEmpty()) {
            String errorType = "Resource not found.";
            String errorMessage = "Doctor whose medical license number is %s was not found.".formatted(medicalLicenseNumber);
            return ErrorResponse.error(Constants.NOT_FOUND_404, errorType, errorMessage);
        }
        return Response.of(optional.get());
    }
}

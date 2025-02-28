package com.api.v2.medical_appointments.utils;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_appointments.domain.MedicalAppointmentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MedicalAppointmentFinderUtil {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public MedicalAppointmentFinderUtil(MedicalAppointmentRepository medicalAppointmentRepository) {
        this.medicalAppointmentRepository = medicalAppointmentRepository;
    }

    public Response<MedicalAppointment> findById(String id) {
        Optional<MedicalAppointment> optional = medicalAppointmentRepository.findById(new ObjectId(id));
        if (optional.isEmpty()) {
            String errorType = "Resource not found.";
            String errorMessage = "Medical appointment whose id is %s was not found".formatted(id);
            return ErrorResponse.error(Constants.NOT_FOUND_404, errorType, errorMessage);
        }
        return Response.of(optional.get());
    }
}

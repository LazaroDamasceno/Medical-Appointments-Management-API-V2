package com.api.v2.medical_slots.utils;

import com.api.v2.common.Constants;
import com.api.v2.common.ErrorResponse;
import com.api.v2.common.Response;
import com.api.v2.medical_appointments.domain.MedicalAppointment;
import com.api.v2.medical_slots.domain.MedicalSlot;
import com.api.v2.medical_slots.domain.MedicalSlotRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MedicalSlotFinderUtil {

    private final MedicalSlotRepository medicalSlotRepository;

    public MedicalSlotFinderUtil(MedicalSlotRepository medicalSlotRepository) {
        this.medicalSlotRepository = medicalSlotRepository;
    }

    public Response<MedicalSlot> findById(String id) {
        Optional<MedicalSlot> optional = medicalSlotRepository.findById(new ObjectId(id));
        if (optional.isEmpty()) {
            String errorType = "Resource not found.";
            String errorMessage = "Medical slot whose id is %s was not found".formatted(id);
            return ErrorResponse.error(Constants.NOT_FOUND_404, errorType, errorMessage);
        }
        return Response.of(optional.get());
    }

    public Response<MedicalSlot> findByMedicalAppointment(MedicalAppointment medicalAppointment) {
        Optional<MedicalSlot> optional = medicalSlotRepository
                .findAll()
                .stream()
                .filter(slot -> slot.getMedicalAppointment().getId().equals(medicalAppointment.getId()))
                .findFirst();
        if (optional.isEmpty()) {
            String errorType = "Resource not found.";
            String errorMessage = "Sought medical slot was not found";
            return ErrorResponse.error(Constants.NOT_FOUND_404, errorType, errorMessage);
        }
        return Response.of(optional.get());
    }
}

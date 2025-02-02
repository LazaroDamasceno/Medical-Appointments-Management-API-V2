package com.api.v2.doctors.services;

import com.api.v2.doctors.dto.DoctorHiringDto;
import com.api.v2.doctors.resources.DoctorResponseResource;
import org.springframework.hateoas.EntityModel;

public interface DoctorHiringService {
    EntityModel<DoctorResponseResource> hire(DoctorHiringDto hiringDto);
}

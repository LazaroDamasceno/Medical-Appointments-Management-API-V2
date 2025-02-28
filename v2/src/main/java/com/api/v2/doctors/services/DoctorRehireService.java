package com.api.v2.doctors.services;

import com.api.v2.common.Response;
import com.api.v2.doctors.resources.DoctorResponseResource;

public interface DoctorRehireService {
    Response<DoctorResponseResource> rehire(String medicalLicenseNumber);
}

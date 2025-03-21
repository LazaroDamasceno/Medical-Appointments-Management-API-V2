package com.api.v2.doctors.utils;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.common.States;

public final class MedicalLicenseNumberFormatter {

    public static MedicalLicenseNumber format(String licenseNumber, String region) {
        States state = States.from(region.toUpperCase());
        return new MedicalLicenseNumber(licenseNumber, state);
    }

}

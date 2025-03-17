package com.api.v2.doctors.utils;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.enums.MedicalRegions;

public final class MedicalLicenseNumberFormatter {

    public static MedicalLicenseNumber format(String licenseNumber, String region) {
        MedicalRegions medicalRegion = MedicalRegions.from(region.toUpperCase());
        return new MedicalLicenseNumber(licenseNumber, medicalRegion);
    }

}

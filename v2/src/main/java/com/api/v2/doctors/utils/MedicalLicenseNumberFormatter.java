package com.api.v2.doctors.utils;

import com.api.v2.doctors.dto.exposed.MedicalLicenseNumber;
import com.api.v2.doctors.enums.Regions;

public final class MedicalLicenseNumberFormatter {

    public static MedicalLicenseNumber format(String licenseNumber, String region) {
        Regions usRegion = Regions.from(region);
        return new MedicalLicenseNumber(licenseNumber, usRegion);
    }

}

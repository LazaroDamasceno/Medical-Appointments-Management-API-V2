package com.api.v2.doctors.enums;

import com.api.v2.doctors.exceptions.NonExistentAmericanRegionException;

import java.util.Arrays;

public enum MedicalRegions {

    AL, AK, AZ, AR, CA, CO, CT, DE, DC,
    FL, GA, HI, ID, IL, IN, IA, KS, KY,
    LA, ME, MD, MA, MI, MN, MS, MO, MT,
    NE, NV, NH, NJ, NM, NY, NC, ND, OH,
    OK, OR, PA, RI, SC, SD, TN, TX, UT,
    VT, VA, WA, WV, WI, WY, AS, GU, MP,
    PR, VI;

    public static MedicalRegions from(String medicalRegion) {
        return Arrays.stream(MedicalRegions
                .values())
                .filter(region -> region.equals(MedicalRegions.valueOf(medicalRegion)))
                .findFirst()
                .orElseThrow(NonExistentAmericanRegionException::new);
    }
}
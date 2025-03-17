package com.api.v2.doctors.enums;

import com.api.v2.doctors.exceptions.NonExistentAmericanRegionException;

import java.util.Arrays;

public enum Regions {

    AL, AK, AZ, AR, CA, CO, CT, DE, DC,
    FL, GA, HI, ID, IL, IN, IA, KS, KY,
    LA, ME, MD, MA, MI, MN, MS, MO, MT,
    NE, NV, NH, NJ, NM, NY, NC, ND, OH,
    OK, OR, PA, RI, SC, SD, TN, TX, UT,
    VT, VA, WA, WV, WI, WY, AS, GU, MP,
    PR, VI;

    public static Regions from(String region) {
        return Arrays.stream(Regions
                .values())
                .filter(r -> r.equals(Regions.valueOf(region)))
                .findAny()
                .orElseThrow(NonExistentAmericanRegionException::new);
    }
}
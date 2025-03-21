package com.api.v2.common;

import com.api.v2.doctors.exceptions.NonExistentStateException;
import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;

public enum States {

    AL, AK, AZ, AR, CA, CO, CT, DE, DC,
    FL, GA, HI, ID, IL, IN, IA, KS, KY,
    LA, ME, MD, MA, MI, MN, MS, MO, MT,
    NE, NV, NH, NJ, NM, NY, NC, ND, OH,
    OK, OR, PA, RI, SC, SD, TN, TX, UT,
    VT, VA, WA, WV, WI, WY;

    public static States from(@NotBlank String state) {
        return Arrays.stream(States
                .values())
                .filter(region -> region.equals(States.valueOf(state.toUpperCase())))
                .findFirst()
                .orElseThrow(NonExistentStateException::new);
    }
}
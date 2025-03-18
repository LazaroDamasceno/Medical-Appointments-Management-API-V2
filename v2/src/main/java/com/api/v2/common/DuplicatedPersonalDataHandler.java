package com.api.v2.common;

import com.api.v2.people.exceptions.DuplicatedEmailException;
import com.api.v2.people.exceptions.DuplicatedSsnException;
import org.springframework.stereotype.Component;

@Component
public class DuplicatedPersonalDataHandler {

    private final DuplicatedPersonalDataChecker duplicatedDataChecker;

    public DuplicatedPersonalDataHandler(DuplicatedPersonalDataChecker duplicatedDataChecker) {
        this.duplicatedDataChecker = duplicatedDataChecker;
    }

    public void handleDuplicatedSsn(String ssn) {
        if (duplicatedDataChecker.isSsnDuplicated(ssn)) {
            throw new DuplicatedSsnException();
        }
    }

    public void handleDuplicatedEmail(String email) {
        if (duplicatedDataChecker.isEmailDuplicated(email)) {
            throw new DuplicatedEmailException();
        }
    }

}

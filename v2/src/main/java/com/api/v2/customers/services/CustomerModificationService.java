package com.api.v2.customers.services;

import com.api.v2.people.dtos.PersonModificationDto;

public interface CustomerModificationService {
    void modify(String id, PersonModificationDto modificationDto);
}

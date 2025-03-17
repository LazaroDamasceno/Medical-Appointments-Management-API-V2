package com.api.v2.common;

public final class AddressResponseMapper {
    public static String mapToString(Address address) {
        return "%s %s, %s, %s".formatted(
                address.zipcode(),
                address.street(),
                address.city(),
                address.state()
        );
    }
}

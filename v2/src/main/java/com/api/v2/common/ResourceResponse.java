package com.api.v2.common;

import org.springframework.hateoas.RepresentationModel;

public class ResourceResponse extends RepresentationModel<ResourceResponse> {

    public ResourceResponse() {
    }

    public static ResourceResponse createEmpty() {
        return new ResourceResponse();
    }
}

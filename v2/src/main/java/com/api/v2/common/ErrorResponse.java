package com.api.v2.common;

public class ErrorResponse {

    private String type;
    private String message;

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse() {}

    private ErrorResponse(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public ErrorResponse of(String type, String message) {
        return new ErrorResponse(type, message);
    }

}

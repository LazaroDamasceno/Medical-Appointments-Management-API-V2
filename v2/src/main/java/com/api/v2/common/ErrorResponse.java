package com.api.v2.common;

public class ErrorResponse<T> extends Response<T> {

    private int httpStatus;
    private String type;
    private String message;

    private ErrorResponse(int httpStatus, String type, String message) {
        this.httpStatus = httpStatus;
        this.type = type;
        this.message = message;
    }

    public static <T> ErrorResponse<T> error(int httpStatus, String type, String message) {
        return new ErrorResponse<>(httpStatus, type, message);
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}

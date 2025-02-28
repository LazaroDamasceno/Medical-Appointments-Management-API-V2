package com.api.v2.common;

public class SuccessfulResponse<T> extends Response<T> {

    private int httpStatus;
    private T data;

    private SuccessfulResponse(int httpStatus, T data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public static <T> SuccessfulResponse<T> success(int httpStatus, T data) {
        return new SuccessfulResponse<>(httpStatus, data);
    }

    public static <T> SuccessfulResponse<T> success(T data) {
        return new SuccessfulResponse<>(Constants.OK_200, data);
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public T getData() {
        return data;
    }
}

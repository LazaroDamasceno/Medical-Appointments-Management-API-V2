package com.api.v2.common;

public class Response<T> {

    private T data;

    public Response() {
    }

    private Response(T data) {
        this.data = data;
    }

    public static <T> Response<T> of(T data) {
        return new Response<>(data);
    }

    public T getData() {
        return data;
    }
}
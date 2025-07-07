package com.example.DEMO_INTEGRATION.DTOs.HttpDTO;

public class HttpResponse {
    private final String result;
    private final String error;
    public HttpResponse(String result, String error) {
        this.result = result;
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }
}

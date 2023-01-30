package com.example.fyp;

public class Response<T> {

    private T body;
    private String error;

    public Response(T body, String error) {
        this.body = body;
        this.error = error;
    }

    public Response(String error) {
        this.error = error;
    }

    public Response(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

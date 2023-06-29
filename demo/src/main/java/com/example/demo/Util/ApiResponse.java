package com.example.demo.Util;

import org.springframework.stereotype.Component;

@Component
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private String error;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String message, String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setError(error);
        return response;
    }
}

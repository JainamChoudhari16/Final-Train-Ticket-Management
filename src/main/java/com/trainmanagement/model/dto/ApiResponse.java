package com.trainmanagement.model.dto;

public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private int statusCode;
    private T data;
    
    // Constructors
    public ApiResponse() {}
    
    public ApiResponse(boolean success, String message, int statusCode, T data) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }
    
    // Static factory methods
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, 200, data);
    }
    
    public static <T> ApiResponse<T> error(String message, int statusCode, T data) {
        return new ApiResponse<>(false, message, statusCode, data);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
} 
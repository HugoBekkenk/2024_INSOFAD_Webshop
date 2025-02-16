package com.example.gamewebshop.dto;

public class ApiResponseDTO {

    private String message;
    private int statusCode;

    public ApiResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

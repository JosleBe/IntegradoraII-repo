package com.utez.integradora.entity.dto;


import lombok.*;

@Data
@Getter
@Setter
public class ApiResponse {
    private int status;
    private String message;
    private Object data;

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(200, message, data);
    }


    public static ApiResponse error(String message) {
        return new ApiResponse(500, message, null);
    }
}

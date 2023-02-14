package com.sparta.hanghaespringexpertlv3.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String message;
    private String httpCode;

    public LoginResponseDto(String message, String httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }
}

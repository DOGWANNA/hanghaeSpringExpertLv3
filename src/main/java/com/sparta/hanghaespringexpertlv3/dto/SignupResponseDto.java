package com.sparta.hanghaespringexpertlv3.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private String message;
    private String httpCode;

    public SignupResponseDto(String message, String httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }
}

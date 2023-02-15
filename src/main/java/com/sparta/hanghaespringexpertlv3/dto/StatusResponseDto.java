package com.sparta.hanghaespringexpertlv3.dto;

import lombok.Getter;

@Getter
public class StatusResponseDto {
    private String message;
    private int code;

    public StatusResponseDto(String message, int code){
        this.message = message;
        this.code = code;
    }
}

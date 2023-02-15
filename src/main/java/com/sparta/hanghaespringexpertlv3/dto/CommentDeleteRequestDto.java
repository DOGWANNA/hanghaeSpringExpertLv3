package com.sparta.hanghaespringexpertlv3.dto;

public class CommentDeleteRequestDto {
    private String message;
    private String httpCode;

    public CommentDeleteRequestDto(String message, String httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }
}

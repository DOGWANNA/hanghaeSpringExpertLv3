package com.sparta.hanghaespringexpertlv3.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaespringexpertlv3.dto.SecurityExceptionDto;
import com.sparta.hanghaespringexpertlv3.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundUserException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<StatusResponseDto> notFoundUser(){
        return new ResponseEntity<>(new StatusResponseDto("회원을 찾지 못했습니다.", 400), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<StatusResponseDto> notFoundPost(){
        return new ResponseEntity<>(new StatusResponseDto("게시물을 찾지 못했습니다.", 400), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity<StatusResponseDto> notFoundComment(){
        return new ResponseEntity<>(new StatusResponseDto("댓글을 찾지 못했습니다.", 400), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StatusResponseDto> notAuthorization(){
        return new ResponseEntity<>(new StatusResponseDto("회원만 수정,삭제 가능합니다.", 400), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<StatusResponseDto> TokenInvalid(){
        return new ResponseEntity<>(new StatusResponseDto("토큰이 유효하지 않습니다.", 400), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(NotMyContentsException.class)
    public ResponseEntity<StatusResponseDto> notMyContents(){
        return new ResponseEntity<>(new StatusResponseDto("본인의 게시물만 수정/삭제 가능합니다.", 400), HttpStatus.BAD_GATEWAY);
    }
}

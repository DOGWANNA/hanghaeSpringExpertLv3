package com.sparta.hanghaespringexpertlv3.exception;

import com.sparta.hanghaespringexpertlv3.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<StatusResponseDto> notFoundUser(){

        return new ResponseEntity<>(new StatusResponseDto("회원을 찾지 못했습니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<StatusResponseDto> notFoundPost(){
        return new ResponseEntity<>(new StatusResponseDto("게시물을 찾지 못했습니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity<StatusResponseDto> notFoundComment(){
        return new ResponseEntity<>(new StatusResponseDto("댓글을 찾지 못했습니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StatusResponseDto> notAuthorization(){
        return new ResponseEntity<>(new StatusResponseDto("회원만 수정,삭제 가능합니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<StatusResponseDto> tokenInvalid(){
        return new ResponseEntity<>(new StatusResponseDto("토큰이 유효하지 않습니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMyContentsException.class)
    public ResponseEntity<StatusResponseDto> notMyContents(){
        return new ResponseEntity<>(new StatusResponseDto("본인의 게시물만 수정/삭제 가능합니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<StatusResponseDto> passWordInvalid(){
        return new ResponseEntity<>(new StatusResponseDto("비밀번호가 틀립니다.", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DupilicateUserException.class)
    public ResponseEntity<StatusResponseDto> dupilicateUser(){
        return new ResponseEntity<>(new StatusResponseDto("중복된 사용자명 입니다..", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAdminPasswordException.class)
    public ResponseEntity<StatusResponseDto> adminPasswordInvalid(){
        return new ResponseEntity<>(new StatusResponseDto("중복된 사용자명 입니다..", 400), HttpStatus.BAD_REQUEST);
    }
}

package com.sparta.hanghaespringexpertlv3.dto;

import com.sparta.hanghaespringexpertlv3.entity.Comment;
import lombok.Getter;
import org.hibernate.annotations.GeneratorType;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String username;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
    }
}

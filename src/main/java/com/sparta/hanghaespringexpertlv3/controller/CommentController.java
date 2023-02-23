package com.sparta.hanghaespringexpertlv3.controller;

import com.sparta.hanghaespringexpertlv3.dto.CommentDeleteRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.CommentRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.CommentResponseDto;
import com.sparta.hanghaespringexpertlv3.dto.StatusResponseDto;
import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Comment_Likes;
import com.sparta.hanghaespringexpertlv3.security.UserDetailsImpl;
import com.sparta.hanghaespringexpertlv3.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/post/comment/{id}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(id, commentRequestDto, userDetails.getUser());
    }

    @PutMapping("/post/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(commentId, commentRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/comment/{commentId}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(commentId, userDetails.getUser());
    }
    @PostMapping("/post/comment/like/{commentId}")
    public ResponseEntity<StatusResponseDto> commentChangeLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.commentChangeLike(commentId, userDetails.getUser());
    }
}

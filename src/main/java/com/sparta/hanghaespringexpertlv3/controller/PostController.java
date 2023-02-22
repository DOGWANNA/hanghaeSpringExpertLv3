package com.sparta.hanghaespringexpertlv3.controller;

import com.sparta.hanghaespringexpertlv3.dto.*;
import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Comment_Likes;
import com.sparta.hanghaespringexpertlv3.entity.Likes;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import com.sparta.hanghaespringexpertlv3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    // 게시물 관련


    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
        return postService.createPost(postRequestDto, request);
    }

    @GetMapping("/post")
    public List<PostCommentSortDto> getPost(HttpServletRequest request){

        return postService.getPost(request);
    }

    @PutMapping("/post/{id}")
    public PostUpdateResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
        return postService.updatePost(id, postRequestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public PostDeleteResponseDto deletePost(@PathVariable Long id, HttpServletRequest request){
        return postService.deletePost(id, request);
    }
    @PostMapping("/post/like/{id}")
    public Likes postChangeLike(@PathVariable Long id, HttpServletRequest request){
        return postService.postChangeLike(id, request);
    }


    //댓글 관련

    @PostMapping("/post/comment/{id}")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request){
        return postService.createComment(id, commentRequestDto, request);
    }

    @PutMapping("/post/comment/{commentId}")
    public Comment updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,  HttpServletRequest request){
        return postService.updateComment(commentId, commentRequestDto, request);
    }

    @DeleteMapping("/post/comment/{commentId}")
    public CommentDeleteRequestDto deleteComment(@PathVariable Long commentId, HttpServletRequest request){
        return postService.deleteComment(commentId, request);
    }
    @PostMapping("/post/comment/like/{commentId}")
    public Comment_Likes commentChangeLike(@PathVariable Long commentId, HttpServletRequest request){
        return postService.commentChangeLike(commentId, request);
    }
}

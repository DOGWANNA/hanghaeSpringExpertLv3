package com.sparta.hanghaespringexpertlv3.controller;

import com.sparta.hanghaespringexpertlv3.dto.*;
import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Comment_Likes;
import com.sparta.hanghaespringexpertlv3.entity.Likes;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import com.sparta.hanghaespringexpertlv3.jwt.JwtAuthFilter;
import com.sparta.hanghaespringexpertlv3.security.UserDetailsImpl;
import com.sparta.hanghaespringexpertlv3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostCommentSortDto>> getPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getPost(userDetails.getUser());
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostUpdateResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updatePost(id, postRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<StatusResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(id, userDetails.getUser());
    }
    @PostMapping("/post/like/{id}")
    public ResponseEntity<StatusResponseDto> postChangeLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.postChangeLike(id, userDetails.getUser());
    }
}

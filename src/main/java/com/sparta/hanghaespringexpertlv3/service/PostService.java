package com.sparta.hanghaespringexpertlv3.service;

import com.sparta.hanghaespringexpertlv3.dto.PostDeleteResponseDto;
import com.sparta.hanghaespringexpertlv3.dto.PostRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.PostResponseDto;
import com.sparta.hanghaespringexpertlv3.dto.PostUpdateResponseDto;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import com.sparta.hanghaespringexpertlv3.entity.User;
import com.sparta.hanghaespringexpertlv3.jwt.JwtUtil;
import com.sparta.hanghaespringexpertlv3.repository.PostRepository;
import com.sparta.hanghaespringexpertlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.saveAndFlush(new Post(postRequestDto, user));

            return new PostResponseDto(post);
        }else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Post> getPost(HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        List<Post> postList = postRepository.findAllByUserId(user.getId());

        return postList;
    }

    @Transactional
    public PostUpdateResponseDto updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );

        post.update(postRequestDto);

        return new PostUpdateResponseDto(postRequestDto);
    }

    @Transactional
    public PostDeleteResponseDto deletePost(Long id, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );
        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(
                () -> new IllegalArgumentException("회원님의 게시물이 아닙니다.")
        );
        postRepository.deleteById(id);

        return new PostDeleteResponseDto("삭제 성공", "200");
    }
}

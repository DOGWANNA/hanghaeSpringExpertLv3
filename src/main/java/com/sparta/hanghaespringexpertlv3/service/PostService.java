package com.sparta.hanghaespringexpertlv3.service;

import com.sparta.hanghaespringexpertlv3.dto.*;
import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import com.sparta.hanghaespringexpertlv3.entity.User;
import com.sparta.hanghaespringexpertlv3.entity.UserRoleEnum;
import com.sparta.hanghaespringexpertlv3.jwt.JwtUtil;
import com.sparta.hanghaespringexpertlv3.repository.CommentRepository;
import com.sparta.hanghaespringexpertlv3.repository.PostRepository;
import com.sparta.hanghaespringexpertlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
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
            Post post = postRepository.saveAndFlush(new Post(postRequestDto, user, new ArrayList<Comment>() {
            }));

            return new PostResponseDto(post);
        }else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPost(HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        //작성 시간별 내림차순으로 게시물 반환
        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for(Post post : postList){
            postResponseDtoList.add(new PostResponseDto(post));
        }

        return postResponseDtoList;
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

    @Transactional
    public Comment createComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        //회원 정보 조회
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        //게시물이 해당 유저의 게시물인지 확인. 게시물 번호 & 회원 번호로 검색
        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다.")
        );

        Comment comment = commentRepository.saveAndFlush(new Comment(
                                                            commentRequestDto.getComment(),user, post));

        return comment;
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);


        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 없습니다.")
        );


        if(comment != null){
            UserRoleEnum userRoleEnum = user.getRole();

            // 사용자 권한 가져와서 ADMIN 이면 전체 수정 가능, USER 면 본인의 댓글만 수정 가능
            if (userRoleEnum == UserRoleEnum.USER){

                // 댓글의 id와 현재 로그인한 user의 id를 비교하여 본인의 댓글만 수정 가능
                if(comment.getUser().getId().equals(user.getId())){
                    comment.update(commentRequestDto);

                }else {
                    throw new IllegalArgumentException("해당 유저의 댓글이 아닙니다.");
                }
            }else {
                comment.update(commentRequestDto);
            }
            return comment;
        }else{
            // 댓글이 없으면 null 반환(어차피 예외 발생함)
            return null;
        }
    }

    public Comment deleteComment(Long commentId, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);


        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 없습니다.")
        );


        if(comment != null){
            UserRoleEnum userRoleEnum = user.getRole();

            // 사용자 권한 가져와서 ADMIN 이면 전체 수정 가능, USER 면 본인의 댓글만 수정 가능
            if (userRoleEnum == UserRoleEnum.USER){

                // 댓글의 id와 현재 로그인한 user의 id를 비교하여 본인의 댓글만 수정 가능
                if(comment.getUser().getId().equals(user.getId())){
                    commentRepository.deleteById(commentId);

                }else {
                    throw new IllegalArgumentException("해당 유저의 댓글이 아닙니다.");
                }
            }else {
                commentRepository.deleteById(commentId);
            }
            return comment;
        }else{
            // 댓글이 없으면 null 반환(어차피 예외 발생함)
            return null;
        }
    }
}

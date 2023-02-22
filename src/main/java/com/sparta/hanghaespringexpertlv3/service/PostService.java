package com.sparta.hanghaespringexpertlv3.service;

import com.sparta.hanghaespringexpertlv3.dto.*;
import com.sparta.hanghaespringexpertlv3.entity.*;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundCommentException;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundPostException;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundUserException;
import com.sparta.hanghaespringexpertlv3.jwt.JwtUtil;
import com.sparta.hanghaespringexpertlv3.repository.*;
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
    private final LikeRepository likeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        Claims claims = jwtUtil.combo(request);

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

            Post post = postRepository.saveAndFlush(new Post(postRequestDto, user, new ArrayList<Comment>() {
            }));

            return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostCommentSortDto> getPost(HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

        //작성 시간별 내림차순으로 게시물 반환
        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<PostCommentSortDto> postCommentSortList = new ArrayList<>();

        for(Post post : postList){
            postCommentSortList.add(new PostCommentSortDto(post));
        }

        return postCommentSortList;
    }

    @Transactional
    public PostUpdateResponseDto updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(NotFoundPostException::new);

        post.update(postRequestDto);

        return new PostUpdateResponseDto(postRequestDto);
    }

    @Transactional
    public PostDeleteResponseDto deletePost(Long id, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(NotFoundPostException::new);

        postRepository.deleteById(id);

        return new PostDeleteResponseDto("삭제 성공", "200");
    }

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        //회원 정보 조회
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

        //게시물이 해당 유저의 게시물인지 확인. 게시물 번호 & 회원 번호로 검색
        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(NotFoundPostException::new);

        Comment comment = commentRepository.saveAndFlush(new Comment(
                                                            commentRequestDto.getComment(),user, post));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);


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

    @Transactional
    public CommentDeleteRequestDto deleteComment(Long commentId, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);


        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);

        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);


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
            return new CommentDeleteRequestDto("댓글 삭제 성공", "200");
        }else{
            // 댓글이 없으면 null 반환(어차피 예외 발생함)
            return null;
        }
    }

    @Transactional
    public Likes postChangeLike(Long id, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);
        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);

        //Likes가 없어도 됨.
        Likes likes = likeRepository.findByIdAndUserId(id, user.getId());

        if(likes != null){
            //좋아요가 있으면 게시물 좋아요 Entity 삭제
            likeRepository.deleteById(likes.getId());
            post.subLike(post.getLikeCount());

        }else{
            //좋아요 엔티티가 게시물 없으면 좋아요 Entity 추가
            likeRepository.saveAndFlush(new Likes(user, post));
            post.addLike(post.getLikeCount());
        }
        //예외 처리 세분화 필요
        return likes;
    }

    public Comment_Likes commentChangeLike(Long commentId, HttpServletRequest request) {
        Claims claims = jwtUtil.combo(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundUserException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        Comment_Likes clikes = commentLikeRepository.findByCommentIdAndUserId(commentId, user.getId());

        if(clikes != null){
            //댓글에 해당 회원의 좋아요가 있으면 게시물 좋아요 Entity 삭제
            commentLikeRepository.deleteById(clikes.getId());
            comment.subLike(comment.getLikeCount());
        }else {
            //댓글에 해당 회원의 좋아요가 있으면 게시물 좋아요 Entity 생성
            commentLikeRepository.saveAndFlush(new Comment_Likes(user, comment));
            comment.addLike(comment.getLikeCount());
        }
        return clikes;
    }
}

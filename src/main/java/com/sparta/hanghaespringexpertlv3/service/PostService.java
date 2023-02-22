package com.sparta.hanghaespringexpertlv3.service;

import com.sparta.hanghaespringexpertlv3.dto.*;
import com.sparta.hanghaespringexpertlv3.entity.*;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundCommentException;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundPostException;
import com.sparta.hanghaespringexpertlv3.exception.NotMyContentsException;
import com.sparta.hanghaespringexpertlv3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ResponseEntity<PostResponseDto> createPost(PostRequestDto postRequestDto, User user) {
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user, new ArrayList<Comment>() {
        }));

        return new ResponseEntity<>(new PostResponseDto(post), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostCommentSortDto>> getPost(User user) {
        //작성 시간별 내림차순으로 게시물 반환
        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<PostCommentSortDto> postCommentSortList = new ArrayList<>();

        for (Post post : postList) {
            postCommentSortList.add(new PostCommentSortDto(post));
        }

        return new ResponseEntity<>(postCommentSortList, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<PostUpdateResponseDto> updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(NotFoundPostException::new);

        checkRole(user, post);
        post.update(postRequestDto);

        return new ResponseEntity<>(new PostUpdateResponseDto(postRequestDto), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<StatusResponseDto> deletePost(Long id, User user) {
        Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(NotFoundPostException::new);

        checkRole(user, post);
        postRepository.deleteById(id);

        return new ResponseEntity<>(new StatusResponseDto("댓글 삭제 성공",200), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<StatusResponseDto> postChangeLike(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);

        //Likes가 없어도 됨.
        Likes likes = likeRepository.findByIdAndUserId(id, user.getId());

        if (likes != null) {
            //좋아요가 있으면 게시물 좋아요 Entity 삭제
            likeRepository.deleteById(likes.getId());
            post.subLike(post.getLikeCount());

        } else {
            //좋아요 엔티티가 게시물 없으면 좋아요 Entity 추가
            likeRepository.saveAndFlush(new Likes(user, post));
            post.addLike(post.getLikeCount());
        }
        //예외 처리 세분화 필요
        return new ResponseEntity<>(new StatusResponseDto("좋아요 클릭",200), HttpStatus.OK);
    }

    public void checkRole(User user, Post post) {
        if((user.getRole() == UserRoleEnum.ADMIN) || (post.getUser().getUsername() == user.getUsername())){
            return;
        }
        throw new NotMyContentsException();
    }
}

package com.sparta.hanghaespringexpertlv3.service;

import com.sparta.hanghaespringexpertlv3.dto.CommentDeleteRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.CommentRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.CommentResponseDto;
import com.sparta.hanghaespringexpertlv3.dto.StatusResponseDto;
import com.sparta.hanghaespringexpertlv3.entity.*;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundCommentException;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundPostException;
import com.sparta.hanghaespringexpertlv3.exception.NotMyContentsException;
import com.sparta.hanghaespringexpertlv3.repository.CommentLikeRepository;
import com.sparta.hanghaespringexpertlv3.repository.CommentRepository;
import com.sparta.hanghaespringexpertlv3.repository.LikeRepository;
import com.sparta.hanghaespringexpertlv3.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(Long id, CommentRequestDto commentRequestDto, User user) {
        //게시물이 해당 유저의 게시물인지 확인. 게시물 번호 & 회원 번호로 검색
        Post post = postRepository.findByIdAndUserId(id , user.getId()).orElseThrow(NotFoundPostException::new);

        Comment comment = commentRepository.saveAndFlush(new Comment(
                commentRequestDto.getComment(),user, post));

        return new ResponseEntity<>(new CommentResponseDto(comment),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        checkRole(user, comment);
        comment.update(commentRequestDto);

        return new ResponseEntity<>(new CommentResponseDto(comment), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<StatusResponseDto> deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        checkRole(user, comment);
        commentRepository.deleteById(commentId);

        return new ResponseEntity<>(new StatusResponseDto("댓글 삭제 성공",200), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<StatusResponseDto> commentChangeLike(Long commentId, User user) {
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
        return new ResponseEntity<>(new StatusResponseDto("좋아요 클릭",200), HttpStatus.OK);
    }

    public void checkRole(User user, Comment comment) {
        if((user.getRole() == UserRoleEnum.ADMIN) || (comment.getUser().getUsername() == user.getUsername())){
            return;
        }
        throw new NotMyContentsException();
    }
}

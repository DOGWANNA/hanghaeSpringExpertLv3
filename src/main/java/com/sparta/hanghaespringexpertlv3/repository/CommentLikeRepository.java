package com.sparta.hanghaespringexpertlv3.repository;


import com.sparta.hanghaespringexpertlv3.entity.Comment_Likes;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentLikeRepository extends JpaRepository<Comment_Likes, Long> {
    Comment_Likes findByCommentIdAndUserId(Long commentId, Long id);
}

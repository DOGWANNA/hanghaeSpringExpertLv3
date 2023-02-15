package com.sparta.hanghaespringexpertlv3.repository;

import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment, Long> {
    List<Comment> findAllByPost(Post post);
}

package com.sparta.hanghaespringexpertlv3.repository;

import com.sparta.hanghaespringexpertlv3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository <Post, Long> {
    List<Post> findAllByUserId(Long id);
    Optional<Post> findByIdAndUserId(Long id, Long id1);

    List<Post> findAllByUserIdOrderByCreatedAtDesc(Long id);
}

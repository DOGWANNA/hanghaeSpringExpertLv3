package com.sparta.hanghaespringexpertlv3.repository;


import com.sparta.hanghaespringexpertlv3.entity.Comment_Likes;
import com.sparta.hanghaespringexpertlv3.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByIdAndUserId(Long id, Long userId);

}

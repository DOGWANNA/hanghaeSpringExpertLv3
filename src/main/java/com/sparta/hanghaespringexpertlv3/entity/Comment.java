package com.sparta.hanghaespringexpertlv3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.hanghaespringexpertlv3.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String comment;

    @Column
    @ColumnDefault("0")
    private long likeCount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public Comment(String comment, User user, Post post) {
        this.comment = comment;
        this.user = user;
        this.post = post;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }

    public void addLike(long likeCount) {this.likeCount = likeCount + 1;}

    public void subLike(long likeCount) {
        this.likeCount = likeCount - 1;
    }
}

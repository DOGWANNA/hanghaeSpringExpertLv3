package com.sparta.hanghaespringexpertlv3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.hanghaespringexpertlv3.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column
    @ColumnDefault("0")
    private long likeCount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Post(PostRequestDto postRequestDto, User user, List<Comment> comments){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = user;
        this.comments = comments;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void addLike(long likeCount) {
        this.likeCount = likeCount + 1;
    }

    public void subLike(long likeCount) {
        this.likeCount = likeCount - 1;
    }
}

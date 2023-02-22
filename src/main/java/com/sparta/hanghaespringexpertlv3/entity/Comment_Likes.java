package com.sparta.hanghaespringexpertlv3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment_Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    public Comment_Likes(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }


}

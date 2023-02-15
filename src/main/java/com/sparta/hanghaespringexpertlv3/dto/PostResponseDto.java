package com.sparta.hanghaespringexpertlv3.dto;

import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class PostResponseDto {
    private String title;
    private String content;

    private String username;
    private List<Comment> comments;


    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();

        if(post.getComments()!=null){
            Collections.sort(post.getComments(),Collections.reverseOrder());
        }
        this.comments = post.getComments();
    }

}

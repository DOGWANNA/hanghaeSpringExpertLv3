package com.sparta.hanghaespringexpertlv3.dto;

import com.sparta.hanghaespringexpertlv3.entity.Comment;
import com.sparta.hanghaespringexpertlv3.entity.Post;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostCommentSortDto {
    private String title;
    private String content;

    private String username;
    private Long likeCount;
    private List<Comment> comments;


    public PostCommentSortDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.likeCount = post.getLikeCount();

        if(post.getComments()!=null){
            Collections.sort(post.getComments(),
                    Collections.reverseOrder(Comparator.comparing(Comment::getCreatedAt)));
//          this.comments = post.getComments().stream().sorted(Collections.reverseOrder(Comparator.comparing(Comment::getCreatedAt))).collect(Collectors.toList());
                    //collect 어떤걸로 묶을것인지. 어떤걸로 반환할것인지.
        }
        this.comments = post.getComments();
    }
}

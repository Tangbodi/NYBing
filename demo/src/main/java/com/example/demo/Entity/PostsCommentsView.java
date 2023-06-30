package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "posts_comments_view")
public class PostsCommentsView {
    @Id
    @Size(max = 36)
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

    @Column(name = "comments")
    private Integer comments;

    @Column(name = "last_comment_at")
    private Instant lastCommentAt;

}
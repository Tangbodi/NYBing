package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "posts_comments_views")
public class PostsCommentsView {
    @Id
    @Size(max = 36)
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

    @NotNull
    @Column(name = "subCategoryId", nullable = false)
    private Integer subCategoryId;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 18)
    @NotNull
    @Column(name = "userName", nullable = false, length = 18)
    private String userName;

    @NotNull
    @Column(name = "views", nullable = false)
    private Integer views;

    @NotNull
    @Column(name = "comments", nullable = false)
    private Integer comments;

    @NotNull
    @Column(name = "last_comment_at", nullable = false)
    private Instant lastCommentAt;

}
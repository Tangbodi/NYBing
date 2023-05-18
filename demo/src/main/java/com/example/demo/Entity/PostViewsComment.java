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
@Table(name = "post_views_comments")
public class PostViewsComment {
    @Id
    @Size(max = 36)
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

    @Column(name = "views")
    private Integer views;

    @Column(name = "comments")
    private Integer comments;

    @Column(name = "last_comment")
    private Instant lastComment;

}
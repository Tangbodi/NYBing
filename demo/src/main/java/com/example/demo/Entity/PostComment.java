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
@Table(name = "post_comments")
public class PostComment {
    @Id
    @Size(max = 36)
    @Column(name = "postId", nullable = false, length = 36)
    private String id;


    @Column(name = "comments")
    private Integer comments;

    @NotNull
    @Column(name = "last_comment")
    private Instant lastComment;

}
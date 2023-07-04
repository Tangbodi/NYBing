package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "users_posts_map")
public class UsersPostsMap {
    @Id
    @Size(max = 36)
    @Column(name = "userId", nullable = false, length = 36)
    private String id;

    @Size(max = 36)
    @Column(name = "postId", length = 36)
    private String postId;

}
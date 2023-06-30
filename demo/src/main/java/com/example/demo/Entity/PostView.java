package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "post_views")
public class PostView {
    @Id
    @Size(max = 36)
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

    @NotNull
    @Column(name = "views", nullable = false)
    private Integer views;

}
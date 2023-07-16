package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @EmbeddedId
    private PostId id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Lob
    @Column(name = "textrender", nullable = false)
    private String textrender;

    @NotNull
    @Column(name = "publishAt", nullable = false)
    private Instant publishAt;

    @Size(max = 18)
    @NotNull
    @Column(name = "userName", nullable = false, length = 18)
    private String userName;

    @Column(name = "ipvFour", columnDefinition = "INT UNSIGNED")
    private Long ipvFour;

    @Size(max = 16)
    @Column(name = "ipvSix", length = 16)
    private String ipvSix;

}
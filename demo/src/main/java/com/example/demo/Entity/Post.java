package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Size(max = 36)
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name="system_uuid",strategy = "uuid")
    @Column(name = "postId", nullable = false, length = 36)
    private String postId;

    @Column(name = "categoryId")
    private Integer categoryId;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "textrender")
    private String textrender;

    @Column(name = "publishAt")
    private Instant publishAt;

    @Size(max = 18)
    @Column(name = "userName", length = 18)
    private String userName;

    @Column(name = "ipvFour", columnDefinition = "INT UNSIGNED")
    private Long ipvFour;

    @Size(max = 16)
    @Column(name = "ipvSix", length = 16)
    private String ipvSix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    @OneToMany(mappedBy = "post")
    @OrderBy("publishAt desc")
    private Set<Comment> comments = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private Set<PostViewsComment> postViewsComments = new LinkedHashSet<>();

}
package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    //@GeneratedValue(generator="uuid")
    //@GenericGenerator(name="uuid",strategy="uuid2")
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

    @NotNull
    @Column(name = "sub_categoryId", nullable = false)
    private Integer subCategoryid;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    //@OrderBy("publishAtdesc")
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

}
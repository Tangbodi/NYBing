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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    //@OrderBy("publishAt desc")
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

}

//   @Id
//    @Size(max = 36)
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name="uuid",strategy = "uuid2")
//    @Column(name = "postId", nullable = false, length = 36)
//    private String id;
//
//    @Column(name = "categoryId")
//    private Integer categoryId;
//
//    @Size(max = 255)
//    @Column(name = "title")
//    private String title;
//
//    @Lob
//    @Column(name = "textrender")
//    private String textrender;
//
//    @Column(name = "publishAt")
//    private Instant publishAt;
//
//    @Size(max = 18)
//    @Column(name = "userName", length = 18)
//    private String userName;
//
//    @Column(name = "ipvFour", columnDefinition = "INT UNSIGNED")
//    private Long ipvFour;
//
//    @Size(max = 16)
//    @Column(name = "ipvSix", length = 16)
//    private String ipvSix;
//
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
//    private User user;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
////    @OrderBy("publishAt desc")
//    private Set<Comment> comments = new LinkedHashSet<>();
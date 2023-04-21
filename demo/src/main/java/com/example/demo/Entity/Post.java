package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Size(max = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false, length = 36)
    private String id;

    @Column(name = "categoryId")
    private Integer categoryId;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "textjson")
    private String textjson;

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
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostViewsComment> postViewsComments = new LinkedHashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextjson() {
        return textjson;
    }

    public void setTextjson(String textjson) {
        this.textjson = textjson;
    }

    public String getTextrender() {
        return textrender;
    }

    public void setTextrender(String textrender) {
        this.textrender = textrender;
    }

    public Instant getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Instant publishAt) {
        this.publishAt = publishAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getIpvFour() {
        return ipvFour;
    }

    public void setIpvFour(Long ipvFour) {
        this.ipvFour = ipvFour;
    }

    public String getIpvSix() {
        return ipvSix;
    }

    public void setIpvSix(String ipvSix) {
        this.ipvSix = ipvSix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<PostViewsComment> getPostViewsComments() {
        return postViewsComments;
    }

    public void setPostViewsComments(Set<PostViewsComment> postViewsComments) {
        this.postViewsComments = postViewsComments;
    }

}
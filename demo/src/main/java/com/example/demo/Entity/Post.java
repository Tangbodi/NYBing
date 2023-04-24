package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(name="system_uuid",strategy = "uuid")//hibernate annotation/32 bits UUID
    @GeneratedValue(generator = "system_uuid")
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

    @JsonIgnore//No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer
    // (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.example.demo.Entity.Post["user"]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private Set<PostViewsComment> postViewsComments = new LinkedHashSet<>();

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
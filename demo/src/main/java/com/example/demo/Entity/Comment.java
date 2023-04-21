package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false)
    private Long id;

    @Column(name = "categoryId")
    private Integer categoryId;

    @Lob
    @Column(name = "commentContent")
    private String commentContent;

    @Size(max = 18)
    @Column(name = "fromId", length = 18)
    private String fromId;

    @Size(max = 18)
    @Column(name = "toId", length = 18)
    private String toId;

    @Column(name = "publishAt")
    private Instant publishAt;

    @Column(name = "from_ipvFour", columnDefinition = "INT UNSIGNED")
    private Long fromIpvfour;

    @Size(max = 16)
    @Column(name = "from_ipvSix", length = 16)
    private String fromIpvsix;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Instant getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Instant publishAt) {
        this.publishAt = publishAt;
    }

    public Long getFromIpvfour() {
        return fromIpvfour;
    }

    public void setFromIpvfour(Long fromIpvfour) {
        this.fromIpvfour = fromIpvfour;
    }

    public String getFromIpvsix() {
        return fromIpvsix;
    }

    public void setFromIpvsix(String fromIpvsix) {
        this.fromIpvsix = fromIpvsix;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
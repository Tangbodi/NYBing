package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "allarticles")
public class Allarticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 20)
    @NotNull
    @Column(name = "author", nullable = false, length = 20)
    private String author;

    @Column(name = "reviewNcomment")
    private Integer reviewNcomment;

    @Column(name = "lastcomment")
    private Instant lastcomment;

    @NotNull
    @Column(name = "userId", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "articleId", nullable = false)
    private Long articleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getReviewNcomment() {
        return reviewNcomment;
    }

    public void setReviewNcomment(Integer reviewNcomment) {
        this.reviewNcomment = reviewNcomment;
    }

    public Instant getLastcomment() {
        return lastcomment;
    }

    public void setLastcomment(Instant lastcomment) {
        this.lastcomment = lastcomment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

}
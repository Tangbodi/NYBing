package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleId", nullable = false)
    private Long id;

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

    @Size(max = 20)
    @Column(name = "author", length = 20)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
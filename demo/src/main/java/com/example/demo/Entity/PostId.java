package com.example.demo.Entity;

import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class PostId implements Serializable {
    private static final long serialVersionUID = -4133933288416012409L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "postId", nullable = false)
    private Long postId;

    @NotNull
    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostId entity = (PostId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, categoryId);
    }

}
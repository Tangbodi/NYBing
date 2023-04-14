package com.example.demo.Entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArticleId implements Serializable {
    private static final long serialVersionUID = 9188936463458774295L;
    @NotNull
    @Column(name = "articleId", nullable = false)
    private Long articleId;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArticleId entity = (ArticleId) o;
        return Objects.equals(this.articleId, entity.articleId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, categoryId);
    }

}
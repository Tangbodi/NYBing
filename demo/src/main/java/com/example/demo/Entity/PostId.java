package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PostId implements Serializable {
    private static final long serialVersionUID = 5684150866626621241L;
    @Size(max = 36)
    @NotNull
    @Column(name = "postId", nullable = false, length = 36)
    private String postId;

    @NotNull
    @Column(name = "subCategoryid", nullable = false)
    private Integer subCategoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostId entity = (PostId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.subCategoryId, entity.subCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, subCategoryId);
    }

}
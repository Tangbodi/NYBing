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
public class SubCategoryPostMapId implements Serializable {
    private static final long serialVersionUID = 780699168058881646L;
    @NotNull
    @Column(name = "sub_categoryId", nullable = false)
    private Short subCategoryid;

    @Size(max = 36)
    @NotNull
    @Column(name = "postId", nullable = false, length = 36)
    private String postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubCategoryPostMapId entity = (SubCategoryPostMapId) o;
        return Objects.equals(this.subCategoryid, entity.subCategoryid) &&
                Objects.equals(this.postId, entity.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subCategoryid, postId);
    }

}
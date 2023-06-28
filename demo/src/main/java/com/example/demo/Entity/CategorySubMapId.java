package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CategorySubMapId implements Serializable {
    private static final long serialVersionUID = -9116303929862410768L;
    @NotNull
    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;

    @NotNull
    @Column(name = "sub_categoryId", nullable = false)
    private Integer subCategoryid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategorySubMapId entity = (CategorySubMapId) o;
        return Objects.equals(this.subCategoryid, entity.subCategoryid) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subCategoryid, categoryId);
    }

}
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
public class ParentCommentsMapId implements Serializable {
    private static final long serialVersionUID = -8103057199701225076L;
    @Size(max = 36)
    @NotNull
    @Column(name = "parent_commentId", nullable = false, length = 36)
    private String parentCommentid;

    @Size(max = 36)
    @NotNull
    @Column(name = "child_commentId", nullable = false, length = 36)
    private String childCommentid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParentCommentsMapId entity = (ParentCommentsMapId) o;
        return Objects.equals(this.childCommentid, entity.childCommentid) &&
                Objects.equals(this.parentCommentid, entity.parentCommentid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(childCommentid, parentCommentid);
    }

}
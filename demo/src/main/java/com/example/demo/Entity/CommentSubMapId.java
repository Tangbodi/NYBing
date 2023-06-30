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
public class CommentSubMapId implements Serializable {
    private static final long serialVersionUID = 7960666428906835074L;
    @NotNull
    @Column(name = "parent_commentId", nullable = false)
    private Integer parentCommentid;

    @NotNull
    @Column(name = "child_commentId", nullable = false)
    private Integer childCommentid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommentSubMapId entity = (CommentSubMapId) o;
        return Objects.equals(this.childCommentid, entity.childCommentid) &&
                Objects.equals(this.parentCommentid, entity.parentCommentid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(childCommentid, parentCommentid);
    }

}
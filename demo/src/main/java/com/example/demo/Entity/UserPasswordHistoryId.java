package com.example.demo.Entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPasswordHistoryId implements Serializable {
    private static final long serialVersionUID = 2676058187368432258L;
    @NotNull
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Size(max = 255)
    @NotNull
    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPasswordHistoryId entity = (UserPasswordHistoryId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.passwordHash, entity.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, passwordHash);
    }

}
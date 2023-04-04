package com.example.demo.Entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class UserLoginHistoryId implements Serializable {
    private static final long serialVersionUID = -8253572421665576993L;
    @NotNull
    @Column(name = "userId", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "loginHist", nullable = false)
    private Instant loginHist;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getLoginHist() {
        return loginHist;
    }

    public void setLoginHist(Instant loginHist) {
        this.loginHist = loginHist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserLoginHistoryId entity = (UserLoginHistoryId) o;
        return Objects.equals(this.loginHist, entity.loginHist) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginHist, userId);
    }

}
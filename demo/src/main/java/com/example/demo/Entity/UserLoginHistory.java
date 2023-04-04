package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "user_login_history")
public class UserLoginHistory {
    @EmbeddedId
    private UserLoginHistoryId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public UserLoginHistoryId getId() {
        return id;
    }

    public void setId(UserLoginHistoryId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
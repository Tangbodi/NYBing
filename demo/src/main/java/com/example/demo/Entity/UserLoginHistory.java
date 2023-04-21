package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "user_login_history")
public class UserLoginHistory {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "loginAt")
    private Instant loginAt;

    @Column(name = "ipvFour", columnDefinition = "INT UNSIGNED")
    private Long ipvFour;

    @Size(max = 16)
    @Column(name = "ipvSix", length = 16)
    private String ipvSix;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Instant loginAt) {
        this.loginAt = loginAt;
    }

    public Long getIpvFour() {
        return ipvFour;
    }

    public void setIpvFour(Long ipvFour) {
        this.ipvFour = ipvFour;
    }

    public String getIpvSix() {
        return ipvSix;
    }

    public void setIpvSix(String ipvSix) {
        this.ipvSix = ipvSix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
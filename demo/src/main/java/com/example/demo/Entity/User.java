package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Size(max = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(name = "userId", nullable = false, length = 36)
    private String id;

    @Size(max = 18)
    @NotNull
    @Column(name = "userName", nullable = false, length = 18)
    private String userName;

    @Size(max = 36)
    @Column(name = "firstName", length = 36)
    private String firstName;

    @Size(max = 36)
    @Column(name = "middleName", length = 36)
    private String middleName;

    @Size(max = 36)
    @Column(name = "lastName", length = 36)
    private String lastName;

    @Size(max = 11)
    @Column(name = "phone", length = 11)
    private String phone;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Size(max = 60)
    @NotNull
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @NotNull
    @Column(name = "registeredAt")
    private Instant registeredAt;


    @Size(max = 15)
    @Column(name = "token", length = 15)
    private String token;

    @Lob
    @Column(name = "verified")
    private String verified;

    @JsonIgnore //if you don't want to return user with modifyHistory then you can add @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserModifyHistory> userModifyHistories = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserLoginHistory> userLoginHistories = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new LinkedHashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public Set<UserModifyHistory> getUserModifyHistories() {
        return userModifyHistories;
    }

    public void setUserModifyHistories(Set<UserModifyHistory> userModifyHistories) {
        this.userModifyHistories = userModifyHistories;
    }

    public Set<UserLoginHistory> getUserLoginHistories() {
        return userLoginHistories;
    }

    public void setUserLoginHistories(Set<UserLoginHistory> userLoginHistories) {
        this.userLoginHistories = userLoginHistories;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

}
package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "userName", nullable = false, length = 20,unique = true)
    private String userName;

    @Size(max = 20)
    @Column(name = "firstName", length = 20)
    private String firstName;

    @Size(max = 20)
    @Column(name = "middleName", length = 20)
    private String middleName;

    @Size(max = 20)
    @Column(name = "lastName", length = 20)
    private String lastName;

    @Size(max = 11)
    @Column(name = "phone", length = 11)
    private String phone;

    @Size(max = 30)
    @Column(name = "email", length = 30)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registeredAt")
    private Instant registeredAt;

    @Column(name = "modifyTime")
    private Instant modifyTime;

    @Size(max = 30)
    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    @OneToMany(mappedBy = "user")
    private Set<UserPasswordHistory> userPasswordHistories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserLoginHistory> userLoginHistories = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Instant getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Instant modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Set<UserPasswordHistory> getUserPasswordHistories() {
        return userPasswordHistories;
    }

    public void setUserPasswordHistories(Set<UserPasswordHistory> userPasswordHistories) {
        this.userPasswordHistories = userPasswordHistories;
    }


    public Set<UserLoginHistory> getUserLoginHistories() {
        return userLoginHistories;
    }

    public void setUserLoginHistories(Set<UserLoginHistory> userLoginHistories) {
        this.userLoginHistories = userLoginHistories;
    }

}
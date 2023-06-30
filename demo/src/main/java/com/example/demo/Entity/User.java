package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Size(max = 36)
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
    @Column(name = "registeredAt", nullable = false)
    private Instant registeredAt;

    @Size(max = 15)
    @Column(name = "token", length = 15)
    private String token;

    @Lob
    @Column(name = "verified")
    private String verified;

}
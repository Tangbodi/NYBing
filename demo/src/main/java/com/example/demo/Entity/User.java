package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(name = "userId", nullable = false, length = 36)
    private String id;
    @Version
    private Long version;
    @Size(max = 31)
    @NotNull
    @Column(name = "userName", nullable = false, length = 31)
    private String userName;

    @Size(max = 31)
    @Column(name = "firstName", length = 31)
    private String firstName;

    @Size(max = 31)
    @Column(name = "middleName", length = 31)
    private String middleName;

    @Size(max = 31)
    @Column(name = "lastName", length = 31)
    private String lastName;

    @Size(max = 11)
    @Column(name = "phone", length = 11)
    private String phone;

    @Size(max = 63)
    @NotNull
    @Column(name = "email", nullable = false, length = 63)
    private String email;

    @Size(max = 63)
    @NotNull
    @Column(name = "password", nullable = false, length = 63)
    private String password;

    @NotNull
    @Column(name = "registeredAt", nullable = false)
    private Instant registeredAt;

    @Size(max = 36)
    @Column(name = "token", length = 36)
    private String token;

    @Lob
    @Column(name = "verified")
    private String verified;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
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
@Table(name = "comments")
public class Comment {
    @Id
    @Size(max = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(name = "commentId", nullable = false, length = 36)
    private String id;

    @Size(max = 4095)
    @NotNull
    @Column(name = "commentContent", nullable = false, length = 4095)
    private String commentContent;


    @Size(max = 36)
    @Column(name = "fromId", length = 36)
    private String fromId;


    @Size(max = 36)
    @Column(name = "toId", length = 36)
    private String toId;

    @Column(name = "from_ipvFour", columnDefinition = "INT UNSIGNED")
    private Long fromIpvfour;

    @Size(max = 16)
    @Column(name = "from_ipvSix", length = 16)
    private String fromIpvsix;

    @Size(max = 31)
    @NotNull
    @Column(name = "fromName", nullable = false, length = 31)
    private String fromName;


    @Size(max = 31)
    @Column(name = "toName", length = 31)
    private String toName;

    @NotNull
    @Column(name = "publishAt", nullable = false)
    private Instant publishAt;

    @Size(max = 36)
    @NotNull
    @Column(name = "postId", nullable = false, length = 36)
    private String postId;

}
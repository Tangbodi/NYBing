package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "categoryId")
    private Integer categoryId;
    @NotNull
    @Lob
    @Column(name = "commentContent")
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

    @Column(name = "parentId")
    private Integer parentId;
    @NotNull
    @Size(max = 18)
    @Column(name = "fromName", length = 18)
    private String fromName;

    @Size(max = 18)
    @Column(name = "toName", length = 18)
    private String toName;
    @NotNull
    @Column(name = "publishAt")
    private Instant publishAt;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

}
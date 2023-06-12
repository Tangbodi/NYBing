package com.example.demo.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "comments_view")
public class CommentsView {
    @Id
    @NotNull
    @Column(name = "commentId", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "categoryId")
    private Integer categoryId;

    @NotNull
    @Lob
    @Column(name = "commentContent")
    private String commentContent;

    @Size(max = 18)
    @Column(name = "fromId", length = 18)
    private String fromId;

    @Size(max = 18)
    @Column(name = "toId", length = 18)
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

    @Size(max = 36)
    @NotNull
    @Column(name = "postId", nullable = false, length = 36)
    private String postId;

}
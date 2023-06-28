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
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatId", nullable = false)
    private Integer id;

    @NotNull
    @Lob
    @Column(name = "chatContent", nullable = false)
    private String chatContent;

    @Size(max = 36)
    @Column(name = "fromId", length = 36)
    private String fromId;

    @Size(max = 36)
    @Column(name = "toId", length = 36)
    private String toId;

    @Column(name = "parentId")
    private Integer parentId;

    @Size(max = 18)
    @NotNull
    @Column(name = "fromName", nullable = false, length = 18)
    private String fromName;

    @Size(max = 18)
    @Column(name = "toName", length = 18)
    private String toName;

    @NotNull
    @Column(name = "publishAt", nullable = false)
    private Instant publishAt;

}
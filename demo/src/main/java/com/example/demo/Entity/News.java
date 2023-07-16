package com.example.demo.Entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "news")
public class News {
    @Id
    @Size(max = 36)
    @Column(name = "newsId", nullable = false, length = 36)
    private String id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "date")
    private LocalDate date;

    @Size(max = 30)
    @Column(name = "source", length = 30)
    private String source;

    @Size(max = 300)
    @Column(name = "title", length = 300)
    private String title;

    @Size(max = 300)
    @Column(name = "itemLink", length = 300)
    private String itemLink;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @Lob
    @Column(name = "content_encoded")
    private String contentEncoded;

    @Size(max = 300)
    @Column(name = "media_content_url", length = 300)
    private String mediaContentUrl;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @Column(name = "pubDate")
    private Instant pubDate;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @Column(name = "run_time")
    private Instant runTime;

}
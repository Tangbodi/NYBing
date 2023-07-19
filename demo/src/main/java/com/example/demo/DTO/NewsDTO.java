package com.example.demo.DTO;

import com.example.demo.Entity.News;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsDTO {

    private String newsId;
    private LocalDate date;
    private String source;
    private String title;
    private String itemLink;
    private String description;
    private String contentEncoded;
    private String mediaContentUrl;
    private Instant pubDate;

    @java.lang.Override
    public java.lang.String toString() {
        return "NewsDTO{" +
                " newsId='" + getNewsId() + "'" +
                " date='" + getDate() + "'" +
                " source='" + getSource() + "'" +
                " title='" + getTitle() + "'" +
                " itemLink='" + getItemLink() + "'" +
                " description='" + getDescription() + "'" +
                " contentEncoded='" + getContentEncoded() + "'" +
                " mediaContentUrl='" + getMediaContentUrl() + "'" +
                " pubDate='" + getPubDate() + "'" +
                "}";
    }

}

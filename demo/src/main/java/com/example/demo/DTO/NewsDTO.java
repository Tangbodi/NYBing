package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

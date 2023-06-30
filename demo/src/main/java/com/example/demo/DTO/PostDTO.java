package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Integer categoryId;
    @Size(min = 1,max = 255)
    private String title;
    @Lob
    private String textrender;
    private String userName;
    private Long ipvFour;
    private String ipvSix;
    private Instant publishAt;
    @java.lang.Override
    public java.lang.String toString() {
        return "PostDTO{" +
                " categoryId='" + getCategoryId() + "'" +
                " title='" + getTitle() + "'" +
                " textrender='" + getTextrender() + "'" +
                " userName='" + getUserName() + "'" +
                " ipvFour='" + getIpvFour() + "'" +
                " ipvSix='" + getIpvSix() + "'" +
                " publishAt='" + getPublishAt() + "'" +
                "}";
    }
}

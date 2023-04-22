package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Integer categoryId;
    private String title;
    private String textjson;
    private String textrender;
    private String userName;
    private Long ipvFour;
    private String ipvSix;

    @java.lang.Override
    public java.lang.String toString() {
        return "TradeDTO{" +
                " categoryId='" + getCategoryId() + "'" +
                " title='" + getTitle() + "'" +
                " textjson='" + getTextjson() + "'" +
                " textrender='" + getTextrender() + "'" +
                " userName='" + getUserName() + "'" +
                " ipvFour='" + getIpvFour() + "'" +
                " ipvSix='" + getIpvSix() + "'" +
                "}";
    }
}

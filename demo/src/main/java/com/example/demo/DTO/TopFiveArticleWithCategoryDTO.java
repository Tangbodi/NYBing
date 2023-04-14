package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopFiveArticleWithCategoryDTO {
    private Long categoryId;
    private String categoryName;
    private Long articleId;
    private String title;


    @java.lang.Override
    public java.lang.String toString() {
        return "TopFiveArticleWithCategoryDTO{" +
                " categoryId='" + getCategoryId() + "'" +
                " categoryName='" + getCategoryName() + "'" +
                " articleId='" + getArticleId() + "'" +
                " title='" + getTitle() + "'" +
                "}";
    }
}

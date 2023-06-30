package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchKeywordDTO {
    @Size(min = 1, max = 50)
    private String keyword;

    @java.lang.Override
    public java.lang.String toString() {
        return "SearchKeyword{" +
                " keyword='" + getKeyword() + "'" +
                "}";
    }
}

package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostsCommentsViewDTO {
    private String postId;
    private Integer subCategoryId;
    private String title;
    private String userName;
    private Integer views;
    private Integer comments;
    private Instant lastCommentAt;
    private String textrender;

    @java.lang.Override
    public java.lang.String toString() {
        return "PostCommentViewDTO{" +
                " postId='" + getPostId() + "'" +
                " subCategoryId='" + getSubCategoryId() + "'" +
                " title='" + getTitle() + "'" +
                " userName='" + getUserName() + "'" +
                " comments='" + getComments() + "'" +
                " views='" + getViews() + "'" +
                " lastCommentAt='" + getLastCommentAt() + "'" +
                " textrender='" + getTextrender() + "'" +
                "}";
    }
}

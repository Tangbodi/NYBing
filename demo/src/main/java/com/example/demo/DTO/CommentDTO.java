package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    @NotBlank
    @Lob
    private String commentContent;
    private String fromId;
    private String toId;
    @Size(min = 1,max = 18)
    private String fromName;
    @Size(min = 1,max = 18)
    private String toName;
    private Long ipvFour;
    private String ipvSix;
    private String parentId;
    private String postId;
    private Instant PublishAt;
    @java.lang.Override
    public java.lang.String toString() {
        return "CommentDTO{" +
                " commentContent='" + getCommentContent() + "'" +
                " fromId='" + getFromId() + "'" +
                " toId='" + getToId() + "'" +
                " fromName='" + getFromName() + "'" +
                " toName='" + getToName() + "'" +
                " ipvFour='" + getIpvFour() + "'" +
                " ipvSix='" + getIpvSix() + "'" +
                " parentId='" + getParentId() + "'" +
                " postId='" + getPostId() + "'" +
                " PublishAt='" + getPublishAt() + "'" +
                "}";
    }
}

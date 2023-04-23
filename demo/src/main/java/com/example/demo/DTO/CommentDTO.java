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
public class CommentDTO {
    private Long commentId;
    private Integer categoryId;
    private String commentContent;
    private String fromId;
    private String toId;
    private String fromName;
    private String toName;
    private Long ipvFour;
    private String ipvSix;
    private Integer parentId;
    private String postId;
    @java.lang.Override
    public java.lang.String toString() {
        return "TradeDTO{" +
                " categoryId='" + getCategoryId() + "'" +
                " commentContent='" + getCommentContent() + "'" +
                " fromId='" + getFromId() + "'" +
                " toId='" + getToId() + "'" +
                " fromName='" + getFromName() + "'" +
                " toName='" + getToName() + "'" +
                " ipvFour='" + getIpvFour() + "'" +
                " ipvSix='" + getIpvSix() + "'" +
                " parentId='" + getParentId() + "'" +
                " postId='" + getPostId() + "'" +
                "}";
    }
}

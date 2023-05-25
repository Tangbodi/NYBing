package com.example.demo.DTO;

import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostWithCommentDTO {
    private Post post;
    private List<Comment> comments;

    public void setComments(List<Comment> comments) {
        this.comments = comments.stream().sorted(Comparator.comparing(Comment::getPublishAt).reversed()).collect(Collectors.toList());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "PostWithCommentDTO{" +
                " post='" + getPost() + "'" +
                " comments='" + getComments() + "'" +
                "}";
    }
}

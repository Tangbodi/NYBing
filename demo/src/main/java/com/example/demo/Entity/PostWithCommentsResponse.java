package com.example.demo.Entity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PostWithCommentsResponse {
    private Post post;
    private List<Comment> comments;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments.stream().sorted(Comparator.comparing(Comment::getPublishAt).reversed()).collect(Collectors.toList());
    }

}

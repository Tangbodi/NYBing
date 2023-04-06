package com.example.demo.Service;

import com.example.demo.DAO.CommentDAO;
import com.example.demo.DTO.CommentBackDTO;
import com.example.demo.Entity.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentBackService {

    private static final Logger logger = LoggerFactory.getLogger(CommentBackService.class);

    @Autowired
    private CommentDAO commentDAO;
    public List<CommentBackDTO> getAllCommentsByArticleId(Long articleId){
        List<Comment> comments = commentDAO.getAllCommentsByArticleId(articleId);
        List<CommentBackDTO> commentBackDTOList = new ArrayList<>();
        CommentBackDTO commentBackDTO = new CommentBackDTO();
        for(Comment comment: comments){
            commentBackDTO.setFromId(comment.getFromId());
            commentBackDTO.setToId(comment.getToId());
            commentBackDTO.setContent(comment.getContent());
            commentBackDTO.setPublishAt(comment.getPublishAt());
            commentBackDTOList.add(commentBackDTO);
        }
        return commentBackDTOList;
    }
}

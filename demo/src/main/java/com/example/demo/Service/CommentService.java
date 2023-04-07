package com.example.demo.Service;

import com.example.demo.DAO.CommentDAO;
import com.example.demo.DTO.CommentFrontDTO;
import com.example.demo.Entity.Article;
import com.example.demo.Entity.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private ArticleService articleService;

    @Transactional
    public List<Comment> getAllCommentsByArticleId(Long articleId){
        return commentDAO.findAllCommentByArticleId(articleId);
    }
    public Comment saveComment(CommentFrontDTO commentFrontDTO,Long articleId){
        Article article = articleService.findArticleById(articleId);
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setArticleType(commentFrontDTO.getArticleType());
        comment.setContent(commentFrontDTO.getContent());
        comment.setFromId(commentFrontDTO.getFromId());
        comment.setToId(commentFrontDTO.getToId());
        comment.setPublishAt(Instant.now());
        return commentDAO.saveComment(comment);
    }
}

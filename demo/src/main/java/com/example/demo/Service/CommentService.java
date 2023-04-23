package com.example.demo.Service;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //    @Transactional
//    public List<Comment> getAllCommentsByArticleId(Long articleId){
//        return commentDAO.findAllCommentByArticleId(articleId);
//    }
    public Comment saveComment(CommentDTO commentDTO){
        logger.info("saving comment::");
        Comment comment = new Comment();
        comment.setCategoryId(commentDTO.getCategoryId());
        comment.setCommentContent(commentDTO.getCommentContent());
        comment.setFromId(comment.getFromId());
        comment.setToId(comment.getToId());
        comment.setFromIpvfour(commentDTO.getIpvFour());
        comment.setFromIpvsix(comment.getFromIpvsix());
        comment.setFromName(comment.getFromName());
        return commentRepository.save(comment);
    }
}

//package com.example.demo.DAO;
//
//import com.example.demo.Repository.CommentRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class CommentDAO {
//    private static final Logger logger = LoggerFactory.getLogger(CommentDAO.class);
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    public Comment saveComment(Comment comment){
//        return this.commentRepository.save(comment);
//    }
//    public List<Comment> findAllCommentByArticleId(Long articleId){
//        return commentRepository.findAllCommentByArticleId(articleId);
//    }
//}

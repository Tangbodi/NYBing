package com.example.demo.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

//@Component
//public class CommentDAO {
//    private static final Logger logger = LoggerFactory.getLogger(CommentDAO.class);
//    @PersistenceContext
//    private EntityManager entityManager;
//    public List<Object[]> getAllCommentsByPostId(String postId){
//        logger.info("Getting all comments by post ID:::"+ postId);
//        try {
//            Query query = this.entityManager.createNativeQuery("SELECT * FROM master.comments WHERE postId =?1");
//            query.setParameter(1, postId);
//            return query.getResultList();
//        } catch (Exception e) {
//            logger.error(e.toString());
//        }
//        return null;
//    }
//}

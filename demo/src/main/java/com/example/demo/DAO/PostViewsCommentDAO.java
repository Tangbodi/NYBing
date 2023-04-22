package com.example.demo.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class PostViewsCommentDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @PersistenceContext
    private EntityManager entityManager;
    public List<Object[]> getPostViewsComment(String postId)
    {
        try {
            Query query = this.entityManager.createNativeQuery("SELECT  posts.postId, posts.categoryId, posts.title, posts.userName, post_views_comments.views, posts.publishAt " +
                    "FROM master.posts LEFT JOIN master.post_views_comments ON post_views_comments.postId = posts.postId where posts.postId =?1");
            query.setParameter(1,postId);

            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }
}

package com.example.demo.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class PostDAO {
    private static final Logger logger = LoggerFactory.getLogger(PostDAO.class);

    @PersistenceContext
    private EntityManager entityManager;
    public List<Object[]> getAllTopFivePostsUnderEveryCategory(){
        logger.info("Getting all top five posts under every category:::");
        try {
            Query query = this.entityManager.createNativeQuery("SELECT p.categoryId, c.categoryName, p.postId, p.title\n" +
                    "FROM (\n" +
                    "  SELECT categoryId, postId, title, \n" +
                    "  ROW_NUMBER() OVER (PARTITION BY categoryId ORDER BY publishAt DESC) as row_num\n" +
                    "  FROM master.posts\n" +
                    ") p\n" +
                    "JOIN master.category c ON p.categoryId = c.categoryId\n" +
                    "WHERE p.row_num <= 5");

            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }
}

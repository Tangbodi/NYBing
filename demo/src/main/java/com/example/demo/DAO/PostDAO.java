package com.example.demo.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;

@Component
public class PostDAO {
    private static final Logger logger = LoggerFactory.getLogger(PostDAO.class);

    @PersistenceContext
    private EntityManager entityManager;
    public List<Object[]> getAllTopFivePostsUnderEveryCategory(){
        logger.info("Getting all top five posts under every category:::");
        try {
            Query query = this.entityManager.createNativeQuery("SELECT p.sub_categoryId, c.sub_categoryName, p.postId, p.title\n" +
                    "FROM (\n" +
                    "  SELECT sub_categoryId, postId, title, \n" +
                    "  ROW_NUMBER() OVER (PARTITION BY sub_categoryId ORDER BY publishAt DESC) as row_num\n" +
                    "  FROM master.posts\n" +
                    ") p\n" +
                    "JOIN master.sub_category c ON p.sub_categoryId = c.sub_categoryId\n" +
                    "WHERE p.row_num <= 5");

            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}

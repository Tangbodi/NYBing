package com.example.demo.DAO;

import com.example.demo.Entity.Post;
import com.example.demo.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Component
public class PostDAO {
    private static final Logger logger = LoggerFactory.getLogger(PostDAO.class);
    @PersistenceContext
    private EntityManager entityManager;
    private final PostRepository postRepository;

    public PostDAO(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Object[]> getTopFivePosts()
    {
        try {
            Query query = this.entityManager.createNativeQuery("SELECT p.categoryId, c.categoryName, p.postId, p.title\n" +
                    "FROM (\n" +
                    "  SELECT categoryId, postId, title, \n" +
                    "  ROW_NUMBER() OVER (PARTITION BY categoryId ORDER BY publishAt DESC) as row_num\n" +
                    "  FROM master.post\n" +
                    ") p\n" +
                    "JOIN master.category c ON p.categoryId = c.categoryId\n" +
                    "WHERE p.row_num <= 5");

           return query.getResultList();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }
    public List<Map<String, Object>>findPostsByCategoryId(Long categoryId){
        try{
            return postRepository.findPostByCategoryId(categoryId);
        }
        catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }
    public Post findPostByPostId(Long postId){
        try{
            Query query = this.entityManager.createNativeQuery("SELECT * FROM master.post where postId =?1",Post.class);
            query.setParameter(1, postId);
            return (Post) query.getSingleResult();
        }
        catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }

}

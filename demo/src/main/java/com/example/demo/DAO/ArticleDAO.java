package com.example.demo.DAO;

import com.example.demo.Entity.Article;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class ArticleDAO {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDAO.class);

    @Autowired
    private ArticleRepository articleRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public Article saveArticle(Article article){
        return articleRepository.save(article);
    }

    public Article findArticleById(Long articleId){
        return articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(articleId));
    }
    public int checkIfUserExists(String email)
    {
        logger.info("Checking if user exists");
        int result = 0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.email = ?1");
            Long resultInLong = (Long) query.getSingleResult();
            result = Math.toIntExact(resultInLong);
        } catch (Exception e) {
            logger.error(e.toString());
            result = 0;
        }

        return result;
    }
}

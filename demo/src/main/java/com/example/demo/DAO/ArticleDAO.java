package com.example.demo.DAO;

import com.example.demo.Entity.Article;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleDAO {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDAO.class);

    @Autowired
    private ArticleRepository articleRepository;

    public Article saveArticle(Article article){
        return articleRepository.save(article);
    }

    public Article findArticleById(Long articleId){
        return articleRepository.findById(articleId).orElseThrow(() -> new UserNotFoundException(articleId));
    }
}

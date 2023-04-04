package com.example.demo.Service;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.Entity.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleDAO articleDAO;
    @Autowired
    private UserService userService;
    public Article saveArticle(Article article) throws IOException {


        article.setPublishAt(Instant.now());
        return articleDAO.saveArticle(article);
    }
}

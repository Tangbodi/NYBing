package com.example.demo.DAO;

import com.example.demo.Entity.Allarticle;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.AllArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class AllArticleDAO {
    private static final Logger logger = LoggerFactory.getLogger(AllArticleDAO.class);

    @Autowired
    private AllArticleRepository allArticleRepository;

    public List<Allarticle> findAllArticle(){
        return allArticleRepository.findAll();
    }

    public Allarticle updateCommentAndViewByArticleId(Long articleId){
        return allArticleRepository.findById(articleId).map(article->{
            article.setLastcomment(Instant.now());
            article.setReviewNcomment(article.getReviewNcomment()+1);
            return allArticleRepository.save(article);
        }).orElseThrow(()-> new UserNotFoundException(articleId));
    }
}

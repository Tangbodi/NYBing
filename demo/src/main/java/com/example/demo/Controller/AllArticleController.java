package com.example.demo.Controller;

import com.example.demo.Entity.Allarticle;
import com.example.demo.Repository.AllArticleRepository;
import com.example.demo.Service.AllArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AllArticleController {
    private static final Logger logger = LoggerFactory.getLogger(AllArticleController.class);


    @Autowired
    private AllArticleService allArticleService;

    @Autowired
    private AllArticleRepository allArticleRepository;
    @GetMapping("/article/all")
    public List<Allarticle> fetchAllArticles(){
        List <Allarticle> allarticleList = allArticleService.getAllArticle();
        return allarticleList;
    }
}

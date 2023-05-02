package com.example.demo.Controller;

import com.example.demo.DTO.NewsApiResultDTO;
import com.example.demo.Service.NewsService;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins ="${ORIGINS}")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;
    @GetMapping("/news/{page}")
    public NewsApiResultDTO getNewsInfo(@PathVariable("page") String page) throws IOException {
        return newsService.getNewsInfo(page);
    }
}

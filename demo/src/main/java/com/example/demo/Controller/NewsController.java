package com.example.demo.Controller;

import com.example.demo.DTO.NewsDTO;
import com.example.demo.Entity.News;
import com.example.demo.Service.NewsService;
import com.example.demo.Util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    private static final String rssFeedUrl = "http://feeds.foxnews.com/foxnews/national";
    private static final String NEWS_CACHE_KEY = "NEWS";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    NewsService newsService;
    //------------------------------------------------------------------------------------------
    @GetMapping("/rss_xml")
    public ResponseEntity< ApiResponse<List<News>>>getAllNewsByPublishDate() throws IOException {
        Jedis jedis = new Jedis("localhost");
        boolean existsInCache = jedis.exists(NEWS_CACHE_KEY);
        if(existsInCache){
            logger.info("NEWS_CACHE exists in Redis cache:::");
            logger.info("Get NEWS from Redis cache:::");
            String json = jedis.get(NEWS_CACHE_KEY);
            List<News> res = objectMapper.readValue(json, List.class);
            ApiResponse<List<News>> apiResponse = ApiResponse.success(res);
            return ResponseEntity.ok(apiResponse);
        }else{
            logger.info("NEWS_CACHE doesn't exist in Redis cache:::");
            logger.info("Catching News from rssFeedUrl:::");
            newsService.proxyXml();
            logger.info("Get NEWS from MySQL:::");
            List<News> NewsList= newsService.getAllNewsByPublishDate();
            if(NewsList!=null){
                ApiResponse<List<News>> apiResponse = ApiResponse.success(NewsList);
                return ResponseEntity.ok(apiResponse);
            }
        }
        ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error", "Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/news/{newsId}")
    public ResponseEntity<ApiResponse<NewsDTO>>getNewsInfo(@PathVariable("newsId")String newsId){
        News news = newsService.getNewsByNewsId(newsId);
        if(news != null){
            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setNewsId(news.getId());
            newsDTO.setTitle(news.getTitle());
            newsDTO.setDate(news.getDate());
            newsDTO.setSource(news.getSource());
            newsDTO.setItemLink(news.getItemLink());
            newsDTO.setDescription(news.getDescription());
            newsDTO.setContentEncoded(news.getContentEncoded());
            newsDTO.setMediaContentUrl(news.getMediaContentUrl());
            newsDTO.setPubDate(news.getPubDate());
            ApiResponse<NewsDTO> apiResponse = ApiResponse.success(newsDTO);
            return ResponseEntity.ok(apiResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "News Not Found", "Not Found"));
    }
}


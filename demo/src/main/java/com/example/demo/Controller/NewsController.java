package com.example.demo.Controller;

import com.example.demo.Entity.News;
import com.example.demo.Service.NewsService;
import com.example.demo.Util.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    private static final String rssFeedUrl = "http://feeds.foxnews.com/foxnews/national";
    private static final String NEWS_CACHE_KEY = "NEWS";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    NewsService newsService;
    //------------------------------------------------------------------------------------------
    @GetMapping(value = "/rss_xml")
    public ResponseEntity< ApiResponse<List<News>>>getNews() throws JsonProcessingException {
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
            logger.info("Get NEWS from MySQL:::");
            List<News> NewsList=  newsService.getAllNewsByPublishDate();
            if(NewsList!=null){
                ApiResponse<List<News>> apiResponse = ApiResponse.success(NewsList);
                return ResponseEntity.ok(apiResponse);
            }else{
                ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }
}


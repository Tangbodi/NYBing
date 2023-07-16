package com.example.demo.Controller;

import com.example.demo.Entity.News;
import com.example.demo.Service.NewsService;
import com.example.demo.Util.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    private static final String rssFeedUrl = "http://feeds.foxnews.com/foxnews/national";

    @Autowired
    NewsService newsService;
    @GetMapping(value = "/rss_xml/writing",produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> proxyXml() {
        logger.info("proxyXml was called:::");
        try {
            //get rss_xml from fox rssFeedUrl
            HttpClient httpClient = HttpClientBuilder.create().build();
            logger.info("HttpClient created:::");
            HttpGet httpGet = new HttpGet(rssFeedUrl);
            logger.info("HttpGet created:::");
            HttpResponse response = httpClient.execute(httpGet);
            logger.info("httpClient executed:::");
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                logger.info("response was ok:::");
                String rssFeed = EntityUtils.toString(response.getEntity(),"UTF-8");
                newsService.saveNewsXmlToDatabase(rssFeed);
                return ResponseEntity.ok(rssFeed);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping(value = "/rss_xml/reading")
    public ResponseEntity< ApiResponse<List<News>>>getNews() throws JsonProcessingException {
        List<News> NewsList=  newsService.getNewsByPublishDate();
       if(!NewsList.isEmpty()){
           ApiResponse<List<News>> apiResponse = ApiResponse.success(NewsList);
           return ResponseEntity.ok(apiResponse);
       }else{
           ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error", "Internal Server Error");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
       }
    }
}


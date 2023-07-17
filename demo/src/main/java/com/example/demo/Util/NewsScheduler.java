package com.example.demo.Util;

import com.example.demo.Entity.News;
import com.example.demo.Repository.NewsRepository;
import com.example.demo.Service.NewsService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsScheduler {
//"0 0 * * * *"
// | | | | | |
// | | | | | +-- Day of the week (0 - 7) (Sunday is 0 and 7)
// | | | | +---- Month (1 - 12)
// | | | +------ Day of the month (1 - 31)
// | | +-------- Hour (0 - 23)
// | +---------- Minute (0 - 59)
// +------------ Second (0 - 59)
    private static final Logger logger = LoggerFactory.getLogger(NewsScheduler.class);
    private static final String rssFeedUrl = "http://feeds.foxnews.com/foxnews/national";

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private RedisCache redisCache;
    @Scheduled(cron = "0 0 */3 * * *")//every 3 hours
    public void NewsSchedulerTask() {
        try {
            logger.info("NewsSchedulerTask started:::");
            logger.info("Parsing rss feed from rssFeedUrl:::");
            HttpClient httpClient = HttpClientBuilder.create().build();
            logger.info("HttpClient created:::");
            HttpGet httpGet = new HttpGet(rssFeedUrl);
            logger.info("HttpGet created:::");
            HttpResponse response = httpClient.execute(httpGet);
            logger.info("httpClient executed:::");
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                logger.info("response was ok:::");
                String rssFeed = EntityUtils.toString(response.getEntity(),"UTF-8");
                if(newsService.saveNewsXmlToDatabase(rssFeed)!=null){
                    logger.info("News saved successfully:::");
                    //every time save news to database, update redis cache
                    List<News> newsList = newsService.getAllNewsByPublishDate();
                    redisCache.updateNewsCache(newsList);
                }
            }
            logger.info("NewsSchedulerTask ended:::");
        } catch (Exception e) {
            logger.error("NewsSchedulerTask:::Exception:::" + e);
        }
    }
}

package com.example.demo.Util;

import com.example.demo.Entity.News;
import com.example.demo.Repository.NewsRepository;
import com.example.demo.Service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    @Scheduled(cron = "0 0 * * * *")//every 1 hour
    public void NewsSchedulerTask() {
        try {
            logger.info("NewsSchedulerTask started:::");
            logger.info("Catching News from rssFeedUrl:::");
            newsService.proxyXml();
            List<News> newsList = newsService.getAllNewsByPublishDate();
            if(newsList!=null){
                //every time save news to database, update redis cache
                logger.info("Updating redis NEWS cache:::");
                redisCache.updateNewsCache(newsList);
            }
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            logger.error("NewsSchedulerTask:::Exception:::" + e);
        }
    }
}

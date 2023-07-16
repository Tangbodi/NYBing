package com.example.demo.Service;

import com.example.demo.Entity.News;
import com.example.demo.Repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private static final String FoxNews = "Fox News";
    private static final String CACHE_KEY = "FOX_NEWS";
    private static final int CACHE_EXPIRATION_SECONDS = 3 * 60 * 60 + 30 * 60; // Three and a half hours
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private NewsRepository newsRepository;
    public void saveNewsXmlToDatabase(String rssFeed){
        //parse xml string
        try{
            // Create a ByteArrayInputStream from the XML string
            logger.info("Create a ByteArrayInputStream from the XML string:::");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(rssFeed.getBytes());

            // Load and parse the XML document
            logger.info("Load and parse the XML document:::");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(inputStream);

            // Get the root element of the XML
            logger.info("Get the root element of the XML:::");
            Element root = document.getDocumentElement();

            //Get the 'item' nodes from the XML
            logger.info("Get the 'item' nodes from the XML:::");
            NodeList itemNodes = root.getElementsByTagName("item");
            //Iterate through the 'item' nodes
            logger.info("Iterate through the 'item' nodes:::");
            for(int i = 0; i < itemNodes.getLength(); i++){
                Element item = (Element) itemNodes.item(i);
                logger.info("create a new News entity:::");
                News news = new News();
                String identifier =item.getElementsByTagName("category").item(0).getTextContent();

                System.out.println("Identifier Number: " + identifier);
                news.setId(identifier);
                logger.info("set title for news:::");
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                System.out.println(title);
                news.setTitle(title);

                logger.info("set today's date for news:::");
                LocalDate today = LocalDate.now();
                news.setDate(today);

                logger.info("set source for news:::");
                news.setSource(FoxNews);

                logger.info("set item_link for news:::");
                String link = item.getElementsByTagName("guid").item(0).getTextContent();
                System.out.println(link);
                news.setItemLink(link);

                logger.info("set description for news:::");
                String description = item.getElementsByTagName("description").item(0).getTextContent();
                System.out.println(description);
                news.setDescription(description);

                logger.info("set content_encoded for news:::");
                String contentEncoded = item.getElementsByTagName("content:encoded").item(0).getTextContent();
                System.out.println(contentEncoded);
                news.setContentEncoded(contentEncoded);

                logger.info("set media_content_url for news:::");
                String mediaContentUrl = item.getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getTextContent();
                System.out.println(mediaContentUrl);
                news.setMediaContentUrl(mediaContentUrl);

                logger.info("set pubDate for news:::");
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                System.out.println("pubDate:::"+pubDate);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");
                TemporalAccessor temporalAccessor = formatter.parse(pubDate);
                Instant publish_date = Instant.from(temporalAccessor);
                System.out.println("publish_date:::"+publish_date);
                news.setPubDate(publish_date);

                logger.info("set run_time for news:::");
                Instant run_time = Instant.now();
                System.out.println(run_time);
                news.setRunTime(run_time);
                newsRepository.save(news);
                logger.info("News saved successfully:::");
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public List<News> getNewsByPublishDate() throws JsonProcessingException {
        Jedis jedis = new Jedis();
        // Check if the news list is already stored in Redis cache
//        jedis.flushAll();
        String cache = jedis.get(CACHE_KEY);
        if (cache != null) {
            // If yes, return the news list from the cache
            logger.info("News list returned from Redis cache:::");
            return objectMapper.readValue(cache, List.class);
        }else{
            logger.info("News list returned from database:::");
            List<News> newsList = newsRepository.findNewsByPublishDate();
            String json = objectMapper.writeValueAsString(newsList);
            // Store the news list in Redis cache
            jedis.set(CACHE_KEY, json);
            jedis.expire(CACHE_KEY, 60); // Set the cache expiration to 1 hour
            return newsList;
        }
    }
}
